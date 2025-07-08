package com.springboot.backend.controller;

import com.springboot.backend.dto.UserDto;
import com.springboot.backend.entity.ResponseDto;
import com.springboot.backend.payload.RegisterResponse;
import com.springboot.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<UserDto>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<RegisterResponse>> createCategory(@RequestBody UserDto UserDto) {
        return ResponseEntity.ok(userService.createUser(UserDto));
    }
}
