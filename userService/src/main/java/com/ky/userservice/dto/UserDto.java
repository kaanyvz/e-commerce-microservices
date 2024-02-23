package com.ky.userservice.dto;

public record UserDto(
        Integer id,
        String firstName,
        String lastName,
        String username,
        String profileImage
) {
}
