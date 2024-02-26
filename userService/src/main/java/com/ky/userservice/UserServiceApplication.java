package com.ky.userservice;

import com.ky.userservice.enumeration.Role;
import com.ky.userservice.enumeration.TokenType;
import com.ky.userservice.model.Token;
import com.ky.userservice.model.User;
import com.ky.userservice.model.UserPrincipal;
import com.ky.userservice.repository.TokenRepository;
import com.ky.userservice.repository.UserRepository;
import com.ky.userservice.service.security.JWTService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public UserServiceApplication( PasswordEncoder passwordEncoder, JWTService jwtService, UserRepository userRepository, TokenRepository tokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public static void main(String[] args) {

        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String email = "admin@gmail.com";
        if (!isMailAlreadyPresent(email)) {
            var user = User.builder()
                    .firstName("ADMIN")
                    .lastName("ADMIN")
                    .email(email)
                    .password(passwordEncoder.encode("1233"))
                    .role(Role.ADMIN)
                    .createdDate(new Date())
                    .profileImage("NO_IMAGE")
                    .isEnabled(true)
                    .isNotLocked(true)
                    .build();
            UserPrincipal userPrincipal = new UserPrincipal(user);
            var savedUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(userPrincipal);
            var refreshToken = jwtService.generateRefreshToken(userPrincipal);
            saveUserToken(savedUser, jwtToken);


            System.out.println("Token created: " + jwtToken);
            System.out.println("Refresh Token created: " + refreshToken);


        } else {
            System.out.println("Admin already exists. Skipping creation.");
        }


    }
    private boolean isMailAlreadyPresent(String email) {
        return userRepository.existsByEmail(email);
    }

    private void saveUserToken(User user, String token) {
        var savedToken = Token.builder()
                .user(user)
                .token(token)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(savedToken);
    }
}
