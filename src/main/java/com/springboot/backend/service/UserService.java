package com.springboot.backend.service;

import com.springboot.backend.dto.UserDto;
import com.springboot.backend.entity.ResponseDto;
import com.springboot.backend.payload.RegisterResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<ResponseDto<UserDto>> getAllUser();
    ResponseDto<UserDto> getMyInfo();
    ResponseDto<UserDto> getUser(Long id);
    ResponseDto<RegisterResponse> createUser(UserDto userDto);
    ResponseDto<UserDto> updateUser(UserDto userDto);
    ResponseDto<UserDto> deleteUser(Long id);
}
