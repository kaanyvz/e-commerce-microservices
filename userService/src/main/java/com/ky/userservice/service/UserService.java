package com.ky.userservice.service;

import com.ky.rabbitservice.producer.MessageProducer;
import com.ky.rabbitservice.request.EmailRequest;
import com.ky.userservice.dto.Me;
import com.ky.userservice.dto.UserCredential;
import com.ky.userservice.dto.UserDto;
import com.ky.userservice.enumeration.Role;
import com.ky.userservice.exc.PasswordMatchException;
import com.ky.userservice.model.User;
import com.ky.userservice.repository.UserRepository;
import com.ky.userservice.request.CreateUserRequest;
import com.ky.userservice.request.UpdatePasswordRequest;
import com.ky.userservice.request.UpdateUserRequest;
import com.ky.userservice.service.security.JWTService;
import com.ky.userservice.service.security.LoginService;
import io.jsonwebtoken.Claims;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final MessageProducer messageProducer;
    private final LoginService loginService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JWTService jwtService,
                       MessageProducer messageProducer,
                       LoginService loginService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.messageProducer = messageProducer;
        this.loginService = loginService;
    }

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

    public void resetPassword(String email){
        User user = findUserByEmail(email);
        String password = RandomStringUtils.randomAlphanumeric(15);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        sendMail(user, password);
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

    //todo - meFunc.
    public Me getMe (String token){
        Claims claims = jwtService.extractAllClaims(token);
        Integer userId = (Integer)  claims.get("userId");
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

    //  PRIVATE METHODS  //

    private void sendMail(User user, String password){
        String message = "Hello" + user.getFirstName() + ", \n\n Your new password is: " + password;
        String subject = "New Password";
        EmailRequest emailRequest = new EmailRequest(message, user.getEmail(), subject);
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
