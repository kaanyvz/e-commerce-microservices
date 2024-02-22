package com.ky.userservice.dto;

import com.ky.userservice.enumeration.Role;

import java.util.List;

public record Me(
        Integer id,
        List<Role> roles,
        String email,
        String firstName,
        String lastName,
        String profileImage
) {
}
