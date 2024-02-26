package com.ky.userservice.controller;

import com.ky.userservice.dto.UserDto;
import com.ky.userservice.model.User;
import com.ky.userservice.request.LoginRequest;
import com.ky.userservice.request.RegisterRequest;
import com.ky.userservice.response.AuthenticationResponse;
import com.ky.userservice.service.UserService;
import com.ky.userservice.service.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

}
