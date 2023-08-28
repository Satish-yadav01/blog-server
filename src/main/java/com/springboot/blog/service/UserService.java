package com.springboot.blog.service;

import com.springboot.blog.payload.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUser();
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email);
    UserDto updateUserById(Long id,UserDto userDto);
    String deleteUserById(Long id);
}
