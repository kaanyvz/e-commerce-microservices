package com.ky.userservice.service;

import com.ky.userservice.dto.Me;
import com.ky.userservice.dto.UserDto;
import com.ky.userservice.model.User;
import com.ky.userservice.repository.UserRepository;
import com.ky.userservice.request.CreateUserRequest;
import com.ky.userservice.request.UpdateUserRequest;
import com.ky.userservice.service.security.JWTService;
import com.ky.userservice.service.security.LoginService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final LoginService loginService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService, LoginService loginService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

    //todo
//    public Me getMe(String token){
//        Base64.getDecoder().decode(token);
//    }

    //  PRIVATE METHODS  //
    private void isEmailAlreadyExists(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            throw new RuntimeException("This email already exists!");
        }
    }

    private String putTemporarilyImage(String email){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/profile" + email).toUriString();
    }

    private String[] splitToken(String token){
        return token.split("\\.");
    }


}
