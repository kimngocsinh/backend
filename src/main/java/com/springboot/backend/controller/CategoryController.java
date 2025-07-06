package com.springboot.backend.controller;

import com.springboot.backend.dto.CategoryDto;
import com.springboot.backend.entity.ResponseDto;
import com.springboot.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<CategoryDto>> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<CategoryDto>> createCategory(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto<CategoryDto>> updateCategory(@PathVariable Long id,
                                                                    @RequestBody CategoryDto categoryDto) {

        categoryDto.setId(id); // đảm bảo DTO có id cần update
        ResponseDto<CategoryDto> response = categoryService.updateCategory(categoryDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteCategory(@PathVariable Long id) {
        ResponseDto<Void> response = categoryService.deleteCategory(id);
        return ResponseEntity.ok(response);
    }
}
