package com.ky.userservice.mapper;

import com.ky.userservice.dto.UserDto;
import com.ky.userservice.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userToUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profileImage(user.getProfileImage())
                .username(user.getEmail())
                .build();
    }
}
