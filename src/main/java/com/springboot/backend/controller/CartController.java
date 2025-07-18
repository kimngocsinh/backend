package com.springboot.backend.controller;

import com.springboot.backend.dto.CartDto;
import com.springboot.backend.dto.request.CartRequest;
import com.springboot.backend.dto.response.ApiResponse;
import com.springboot.backend.dto.response.CartListResponse;
import com.springboot.backend.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartListResponse>> getAllCart(HttpServletRequest request) {
        return ResponseEntity.ok(cartService.getAllCarts(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCart(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(cartService.deleteCart(id, request));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartDto>> addToCart(@RequestBody  CartRequest cartRequest, HttpServletRequest req) {
        return ResponseEntity.ok(cartService.addToCart(cartRequest, req));
    }
}
