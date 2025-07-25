package com.springboot.backend.controller;

import com.springboot.backend.dto.UserDto;
import com.springboot.backend.dto.response.ApiResponse;
import com.springboot.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id, HttpServletRequest request) {

        // Chứa thông tin user đang đăng nhập
        SecurityContext securityContext = SecurityContextHolder.getContext();

        // Lấy thông tin đăng nhập
//        var authentication = securityContext.getAuthentication();

        return ResponseEntity.ok(userService.getUser(id, request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable Long id, @ RequestBody UserDto userDto,
                                                           HttpServletRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, userDto, request));
    }

}
