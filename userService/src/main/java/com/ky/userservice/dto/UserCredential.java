package com.ky.userservice.dto;

public record UserCredential(
        Integer id,
        String firstName,
        String lastName,
        String email
){
}
