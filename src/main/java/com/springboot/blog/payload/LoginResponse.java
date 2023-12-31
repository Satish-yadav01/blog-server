package com.springboot.blog.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String name;
    private String username;
    private String email;
    private String profileImagePath;
    private String accessToken;
    private String tokenType = "Bearer";
}
