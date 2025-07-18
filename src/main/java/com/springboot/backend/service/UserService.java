package com.springboot.backend.service;

import com.springboot.backend.dto.UserDto;
import com.springboot.backend.dto.response.ApiResponse;
import com.springboot.backend.payload.RegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    ApiResponse<List<UserDto>> getAllUser(HttpServletRequest request);
    ApiResponse<UserDto> getMyInfo(HttpServletRequest request);
    ApiResponse<UserDto> getUser(Long id, HttpServletRequest request);
    ApiResponse<RegisterResponse> createUser(UserDto userDto, HttpServletRequest request);
    ApiResponse<UserDto> updateUser(Long id, UserDto userDto, HttpServletRequest request);
    ApiResponse<UserDto> deleteUser(Long id, HttpServletRequest request);
}
