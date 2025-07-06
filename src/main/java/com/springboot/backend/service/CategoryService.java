package com.springboot.backend.service;

import com.springboot.backend.dto.CategoryDto;
import com.springboot.backend.entity.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    ResponseDto<CategoryDto> getCategory(Long id);

    ResponseDto<CategoryDto> createCategory(CategoryDto categoryDto);

    ResponseDto<CategoryDto> updateCategory(CategoryDto categoryDto);

    ResponseDto<Void> deleteCategory(Long id);
}
