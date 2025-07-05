package com.springboot.backend.service;

import com.springboot.backend.dto.CategoryDto;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    CategoryDto getCategory(Long id);

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(Long id);
}
