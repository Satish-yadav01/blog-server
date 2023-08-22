package com.springboot.blog.service;

import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.LoginResponse;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.payload.RegisterResponse;

import java.io.IOException;

public interface   AuthService {
    LoginResponse login(LoginDto loginDto);

    RegisterResponse register(RegisterDto registerDto) throws IOException;
}
