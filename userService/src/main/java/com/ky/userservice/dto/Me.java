package com.ky.userservice.dto;

import com.ky.userservice.enumeration.Role;
import lombok.Builder;

import java.util.List;

@Builder
public record Me(
        Integer id,
        List<Role> roles,
        String email,
        String firstName,
        String lastName,
        String profileImage
) {
}
