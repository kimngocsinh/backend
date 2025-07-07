package com.springboot.backend.payload;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class LoginResponse {

    private String accessToken;

    private String tokenType;

    private Map<String, String> errors = new HashMap<>();  // Map lỗi nếu có

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
    }

    public LoginResponse(Map<String, String> errors) {
        this.errors = errors;
    }
}
