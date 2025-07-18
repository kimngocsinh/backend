package com.springboot.backend.service;

import com.springboot.backend.dto.response.ApiResponse;
import com.springboot.backend.dto.CategoryDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    ApiResponse<CategoryDto> getCategory(Long id, HttpServletRequest request);

    ApiResponse<CategoryDto> createCategory(CategoryDto categoryDto, HttpServletRequest request);

    ApiResponse<CategoryDto> updateCategory(CategoryDto categoryDto, HttpServletRequest request);

    ApiResponse<Void> deleteCategory(Long id, HttpServletRequest request);
}
