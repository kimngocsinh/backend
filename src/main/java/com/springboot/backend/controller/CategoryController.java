package com.springboot.backend.controller;

import com.springboot.backend.dto.response.ApiResponse;
import com.springboot.backend.dto.CategoryDto;
import com.springboot.backend.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategory(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(categoryService.getCategory(id, request));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(@RequestBody CategoryDto categoryDto, HttpServletRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto, request));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(@PathVariable Long id,
                                                                    @RequestBody CategoryDto categoryDto,
                                                                    HttpServletRequest request) {

        categoryDto.setId(id); // đảm bảo DTO có id cần update
        return ResponseEntity.ok(categoryService.updateCategory(categoryDto, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(categoryService.deleteCategory(id, request));
    }
}
