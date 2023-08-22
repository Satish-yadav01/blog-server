package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.LoginResponse;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.payload.RegisterResponse;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.service.AuthService;
//import jakarta.persistence.criteria.Path;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${image.upload.path}")
    private String profileImagePath;

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    private ServletContext servletContext; // Inject ServletContext

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public LoginResponse login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        //find user and store in RegisterOrLoginResponse
        Optional<User> user = userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail());


        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setName(user.get().getName());
        loginResponse.setEmail(user.get().getEmail());
        loginResponse.setUsername(user.get().getUsername());
        loginResponse.setProfileImagePath(user.get().getProfileImagePath());
        loginResponse.setAccessToken(token);
        loginResponse.setTokenType("Bearer");
        return loginResponse;
    }

    @Override
    public RegisterResponse register(RegisterDto registerDto) throws IOException {

        // add check for username exists in database
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        // add check for email exists in database
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }


        String imageName = System.currentTimeMillis() + "_" + registerDto.getProfileImage().getOriginalFilename();
//        String absoluteImagePath = servletContext.getRealPath(profileImagePath) + imageName;

        Path imagePath = Paths.get(profileImagePath + imageName);

        // Log the absolute path
        System.out.println("Absolute Path: " + imagePath.toAbsolutePath());

        Files.copy(registerDto.getProfileImage().getInputStream(), imagePath);

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setProfileImagePath(imageName);

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setName(user.getName());
        registerResponse.setEmail(user.getEmail());
        registerResponse.setUsername(user.getUsername());
        registerResponse.setProfileImagePath(user.getProfileImagePath());

        return registerResponse;
    }
}
