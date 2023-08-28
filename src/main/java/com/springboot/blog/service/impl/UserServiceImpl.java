package com.springboot.blog.service.impl;

import com.springboot.blog.entity.User;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.UserDto;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> users = userRepository.findAll();

        List<UserDto> userDtos = users.stream().map(user -> mapToUserDto(user)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        UserDto userDto = mapToUserDto(user);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email is not valid or present"));
        UserDto userDto = mapToUserDto(user);
        return userDto;
    }

    @Override
    public UserDto updateUserById(Long id,UserDto userDto) {
        userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("userid is not valid"));

        User user = mapToUserEntity(userDto);
        User savedUser = userRepository.save(user);
        return mapToUserDto(savedUser);
    }

    @Override
    public String deleteUserById(Long id) {
        userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("userid is not valid"));

        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = mapper.map(user, UserDto.class);
        return userDto;
    }

    private User mapToUserEntity(UserDto userDto){
        User user = mapper.map(userDto, User.class);
        return user;
    }
}
