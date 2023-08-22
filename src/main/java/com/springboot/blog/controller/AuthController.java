package com.springboot.blog.controller;

import com.springboot.blog.payload.*;
import com.springboot.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Build Login REST API
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto){
        LoginResponse login = authService.login(loginDto);

//        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
//        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(login);
    }

    // Build Register REST API
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<RegisterResponse> register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("profileImage") MultipartFile profileImage) throws IOException {

        RegisterDto registerDto = new RegisterDto();
        registerDto.setName(name);
        registerDto.setEmail(email);
        registerDto.setUsername(username);
        registerDto.setPassword(password);
        registerDto.setProfileImage(profileImage);

        RegisterResponse response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}