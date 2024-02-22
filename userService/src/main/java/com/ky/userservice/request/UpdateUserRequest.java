package com.ky.userservice.request;

import lombok.Getter;

@Getter
public class UpdateUserRequest {
    private String email;
    private String password;
    private String firstName;
    private  String lastName;
    private String profileImage;
}
