package com.ky.userservice.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ky.userservice.dto.UserDto;
import com.ky.userservice.enumeration.Role;
import com.ky.userservice.enumeration.TokenType;
import com.ky.userservice.exc.EmailDuplicationError;
import com.ky.userservice.mapper.UserMapper;
import com.ky.userservice.model.Token;
import com.ky.userservice.model.User;
import com.ky.userservice.model.UserPrincipal;
import com.ky.userservice.repository.TokenRepository;
import com.ky.userservice.repository.UserRepository;
import com.ky.userservice.request.LoginRequest;
import com.ky.userservice.request.RegisterRequest;
import com.ky.userservice.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Date;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthenticationService(UserRepository userRepository,
                                 TokenRepository tokenRepository,
                                 JWTService jwtService,
                                 AuthenticationManager authenticationManager,
                                 PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserDto register(RegisterRequest request){
        if(isMailAlreadyPresent(request.getEmail())){
            throw new EmailDuplicationError("This email has already used before in this system => " + request.getEmail());
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .createdDate(new Date())
                .profileImage(putTemporarilyImage(request.getEmail()))
                .isEnabled(true)
                .isNotLocked(true)
                .build();

        User savedUser = userRepository.save(user);
        return userMapper.userToUserDto(savedUser);

    }

    public AuthenticationResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User could not found..."));

        user.setLastLoginShow(user.getLastLogin());
        user.setLastLogin(new Date());

        UserPrincipal userPrincipal = new UserPrincipal(user);
        var jwtToken = jwtService.generateToken(userPrincipal);
        var refreshToken = jwtService.generateRefreshToken(userPrincipal);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail != null){

            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            UserPrincipal userPrincipal = new UserPrincipal(user);
            if(jwtService.isTokenValid(refreshToken, userPrincipal)){
                var accessToken = jwtService.generateToken(userPrincipal);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }

    }

    //--  PRIVATE METHODS --//
    private void saveUserToken(User user, String jwtToken){
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private boolean isMailAlreadyPresent(String mail){
        return userRepository.existsByEmail(mail);
    }
    private String putTemporarilyImage(String email){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/profile" + email).toUriString();
    }

}
