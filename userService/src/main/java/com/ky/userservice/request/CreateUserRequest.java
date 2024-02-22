package com.ky.userservice.request;

import com.ky.userservice.enumeration.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CreateUserRequest {
    @NotNull
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String password;
    private Role role;
    private MultipartFile profileImage;
}
