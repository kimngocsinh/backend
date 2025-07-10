package com.springboot.backend.service;

import com.springboot.backend.dto.UserDto;
import com.springboot.backend.entity.ResponseDto;
import com.springboot.backend.entity.response.ApiResponse;
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
    ResponseDto<UserDto> updateUser(UserDto userDto);
    ResponseDto<UserDto> deleteUser(Long id);
}
