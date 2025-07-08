package com.springboot.backend.payload;

import com.springboot.backend.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private UserDto user;
    private String token;
}
