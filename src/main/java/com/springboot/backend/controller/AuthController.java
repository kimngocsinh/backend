package com.springboot.backend.controller;

import com.springboot.backend.config.Constants;
import com.springboot.backend.dto.ApiResponse;
import com.springboot.backend.dto.UserDto;
import com.springboot.backend.entity.User;
import com.springboot.backend.payload.LoginRequest;
import com.springboot.backend.payload.LoginResponse;
import com.springboot.backend.payload.RegisterResponse;
import com.springboot.backend.service.UserService;
import com.springboot.backend.service.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;  // Quản lý xác thực
    private final JwtService jwtService;  // Service xử lý JWT
    private final UserService userService;

    /**
     * API login
     * @param loginRequest
     * @return token hoặc lỗi
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User userDetails = (User) authentication.getPrincipal();

            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new LoginResponse(token));
        } catch (AuthenticationException e) {
            Map<String, String> errors = Map.of(Constants.ERROR, Constants.LOGIN_INVALID);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(errors));
        }
    }

    /**
     * API register/ đăng ký
     * @param UserDto
     * @param request
     * @return ApiResponse<RegisterResponse>
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid  @RequestBody UserDto UserDto,
                                                                  HttpServletRequest request) {
        return ResponseEntity.ok(userService.createUser(UserDto, request));
    }
}
