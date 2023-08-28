package com.springboot.blog.controller;

import com.springboot.blog.payload.UserDto;
import com.springboot.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUser(){
        List<UserDto> allUser = userService.getAllUser();
        return ResponseEntity.ok(allUser);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id){
        UserDto userById = userService.getUserById(id);
        return new ResponseEntity<>(userById,HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam("email") String email){
        UserDto userById = userService.getUserByEmail(email);
        return new ResponseEntity<>(userById,HttpStatus.OK);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable("id") Long id,@RequestBody UserDto userDto){
        UserDto savedUserDto = userService.updateUserById(id, userDto);
        return new ResponseEntity<>(savedUserDto,HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id){
        String s = userService.deleteUserById(id);
        return new ResponseEntity<>(s,HttpStatus.OK);
    }
}
