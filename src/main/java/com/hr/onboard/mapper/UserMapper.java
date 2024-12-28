package com.hr.onboard.mapper;

import com.hr.onboard.dto.UserDto;
import com.hr.onboard.entity.auth.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user){
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
    }

    public static User mapToUser(UserDto userDto){
        return new User (
                userDto.setId();
//                userDto.getId(),
//                userDto.getName(),
//                userDto.getEmail(),
//                userDto.getPassword()
        );
    }
}
