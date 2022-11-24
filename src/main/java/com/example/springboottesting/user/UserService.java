package com.example.springboottesting.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserDto addUser(AddUserRequestDto addUserRequestDto) {
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setEmail(addUserRequestDto.getEmail());
        userDto.setFirstName(addUserRequestDto.getFirstName());
        return userDto;
    }
}
