package com.springboot.backend.service;

import com.springboot.backend.dto.CartDto;
import com.springboot.backend.dto.request.CartRequest;
import com.springboot.backend.dto.response.ApiResponse;
import com.springboot.backend.dto.response.CartListResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    ApiResponse<CartListResponse> getAllCarts (HttpServletRequest req);
    ApiResponse<Void> deleteCart(Long id, HttpServletRequest req);
    ApiResponse<CartDto> addToCart(CartRequest cartRequest, HttpServletRequest req);
}
