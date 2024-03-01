package com.ky.userservice.service;

import com.ky.rabbitservice.producer.MessageProducer;
import com.ky.rabbitservice.request.EmailRequest;
import com.ky.userservice.dto.Me;
import com.ky.userservice.dto.UserCredential;
import com.ky.userservice.dto.UserDto;
import com.ky.userservice.enumeration.Role;
import com.ky.userservice.exc.PasswordMatchException;
import com.ky.userservice.model.File;
import com.ky.userservice.model.User;
import com.ky.userservice.repository.FileRepository;
import com.ky.userservice.repository.UserRepository;
import com.ky.userservice.request.CreateUserRequest;
import com.ky.userservice.request.UpdatePasswordRequest;
import com.ky.userservice.request.UpdateUserRequest;
import com.ky.userservice.service.security.JWTService;
import com.ky.userservice.service.security.LoginService;
import io.jsonwebtoken.Claims;


import jakarta.annotation.PostConstruct;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final MessageProducer messageProducer;
    private final LoginService loginService;
    private final FileRepository fileRepository;
    private String FOLDER_PATH;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JWTService jwtService,
                       MessageProducer messageProducer,
                       LoginService loginService, FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.messageProducer = messageProducer;
        this.loginService = loginService;
        this.fileRepository = fileRepository;
    }

    // only admin can use
    public User createNewUser(CreateUserRequest request){
        isEmailAlreadyExists(request.getEmail());
        var user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .role(request.getRole())
                .isEnabled(request.isEnabled())
                .isNotLocked(request.isNonLocked())
                .password(passwordEncoder.encode(request.getPassword()))
                .profileImage(putTemporarilyImage(request.getEmail()))
                .createdDate(new Date())
                .build();
        userRepository.save(user);
        return user;
    }

    public Me getMe (String token) {
        Claims claims = jwtService.extractAllClaims(token);
        Integer userId = (Integer) claims.get("userId");
        String firstName = (String) claims.get("firstname");
        String lastName = (String) claims.get("lastname");
        String profileImage = (String) claims.get("profileImgUrl");
        String email = (String) claims.get("email");

        List<String> authorityNames = ((List<Map<String, String>>) claims.get("authorities")).stream()
                .map(m -> m.get("authority"))
                .collect(Collectors.toList());

        List<Role> roles = authorityNames.stream()
                .map(authority -> Role.valueOf(authority.substring("ROLE_".length())))
                .collect(Collectors.toList());

        return new Me(
                userId,
                roles,
                email,
                firstName,
                lastName,
                profileImage
        );
    }


    //UPDATE METHODS//
    public User updateUser(UpdateUserRequest request){
        User user = userRepository.findUserByEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setProfileImage(user.getProfileImage());
        //todo - add more
        return userRepository.save(user);
    }
    public User updatePassword(UpdatePasswordRequest request, String token){
        Claims claims = jwtService.extractAllClaims(token);
        String email = (String) claims.get("email");
        User currentUser = findUserByEmail(email);

        if(passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())){
            currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }else{
            throw new PasswordMatchException("Passwords did not match with each other");
        }
        return userRepository.save(currentUser);
    }



    //PASSWORD METHODS
    public void resetPassword(String email){
        User user = findUserByEmail(email);
        String password = RandomStringUtils.randomAlphanumeric(15);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        String message = "Hello " + user.getFirstName() + ", \n\nYour new password is: " + password;
        String subject = "New Password";
        EmailRequest emailRequest = new EmailRequest(message, user.getEmail(), subject);
        sendMail(emailRequest);
    }

    public UserDto getUserByEmail(String email){
        User user = userRepository.findUserByEmail(email);
        return UserDto.builder()
                .id(user.getId())
                .username(user.getEmail())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .profileImage(user.getProfileImage())
                .build();
    }

    public String deleteUser(String email){
        User user = findUserByEmail(email);
        userRepository.deleteById(user.getId());
        return "User has deleted successfully!";
    }

    protected File findFileById(String id){
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not found by id.."));
    }

    @PostConstruct
    public void init() {
        String currentWorkingDirectory = System.getProperty("user.dir");

        FOLDER_PATH = currentWorkingDirectory + "/file-storage/src/main/resources/attachments";

        java.io.File targetFolder = new java.io.File(FOLDER_PATH);

        if (!targetFolder.exists()) {
            boolean directoriesCreated = targetFolder.mkdirs();
            if (!directoriesCreated) {
                throw new RuntimeException("Error");
            }
        }
    }

    public String uploadImgToTheSystem(String email, MultipartFile file){
        User user = findUserByEmail(email);
        String uuid = UUID.randomUUID().toString();
        String filePath = FOLDER_PATH + "/" + uuid;
        System.out.println(filePath);

        try {
            file.transferTo(new java.io.File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("ERROR!!!" + e.getMessage());
        }

        File uploadedFile = File.builder()
                .id(uuid)
                .type(file.getContentType())
                .filePath(filePath)
                .build();


        user.setProfileImg(uploadedFile);

        userRepository.save(user);

        return "Image has successfully added.";
    }

    public byte[] downloadImageFromFileSystem(String id) {
        try {
            return Files.readAllBytes(new java.io.File(findFileById(id)
                    .getFilePath()).toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error while downloading file...");
        }
    }


    //  PRIVATE METHODS  //

    private void sendMail(EmailRequest emailRequest){
        messageProducer.publish(
                emailRequest,
                "notification.exchange",
                "send.email.routing-key"
        );
    }

    private void isEmailAlreadyExists(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            throw new RuntimeException("This email already exists!");
        }
    }

    //todo - apply the if suggestion
    private void validateLogin(User user){
        if(user.isNotLocked()){
            if(loginService.isExceededMaxAttempts(user.getEmail())){
                user.setNotLocked(false);
            }else{
                user.setNotLocked(true);
            }
        }else {
            loginService.removeLoginCache(user.getEmail());
        }
    }

    private UserCredential getUserCredentials(Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return new UserCredential(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    private String putTemporarilyImage(String email){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/profile" + email).toUriString();
    }

    private User findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }

}
