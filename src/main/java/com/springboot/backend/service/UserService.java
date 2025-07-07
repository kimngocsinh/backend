package com.springboot.backend.service;

import com.springboot.backend.dto.UserDto;
import com.springboot.backend.entity.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    ResponseDto<UserDto> getUser(Long id);
    ResponseDto<UserDto> createUser(UserDto userDto);
    ResponseDto<UserDto> updateUser(UserDto userDto);
    ResponseDto<UserDto> deleteUser(Long id);
}
