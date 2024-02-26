package com.ky.userservice.dto;

import lombok.Builder;

@Builder
public record UserDto(
        Integer id,
        String firstName,
        String lastName,
        String username,
        String profileImage
) {
}
