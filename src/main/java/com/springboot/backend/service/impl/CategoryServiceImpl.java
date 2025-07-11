package com.springboot.backend.service.impl;

import com.springboot.backend.config.Constants;
import com.springboot.backend.dto.ApiResponse;
import com.springboot.backend.dto.CategoryDto;
import com.springboot.backend.entity.Category;
import com.springboot.backend.mapper.CategoryMapper;
import com.springboot.backend.repository.CategoryRepository;
import com.springboot.backend.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    /**
     * Get ban ghi category theo id
     * @param id
     * @return ApiResponse<CategoryDto>
     */
    @Override
    public ApiResponse<CategoryDto> getCategory(Long id, HttpServletRequest request) {
        Optional<Category> optional = categoryRepository.findById(id);

        if (optional.isEmpty()) {
            return ApiResponse.error(null, Constants.CATEGORY_NOT_FOUND,  request.getRequestURI());
        }

        Category category = optional.get();
        CategoryDto dto = categoryMapper.toCategoryDto(category);
        return ApiResponse.success(dto, request.getRequestURI());
    }

    /**
     * Tao moi category va insert vao DB
     * @param categoryDto
     * @return CategoryDto
     */
    @Override
    public ApiResponse<CategoryDto> createCategory(CategoryDto categoryDto, HttpServletRequest request) {
        Category category = categoryMapper.toCategory(categoryDto);
        Category saved = categoryRepository.save(category);

        CategoryDto result = categoryMapper.toCategoryDto(saved);
        return ApiResponse.success(result, request.getRequestURI());
    }

    /**
     * Update category
     * @param categoryDto
     * @return ApiResponse<CategoryDto>
     */
    @Override
    public ApiResponse<CategoryDto> updateCategory(CategoryDto categoryDto, HttpServletRequest request) {
        Optional<Category> optional = categoryRepository.findById(categoryDto.getId());
        if (optional.isEmpty()) {
            return ApiResponse.error(null, Constants.CATEGORY_NOT_FOUND,  request.getRequestURI());
        }

        Category category = optional.get();
        Category updated = categoryRepository.save(category);

        CategoryDto result = categoryMapper.toCategoryDto(updated);
        return ApiResponse.success(result, request.getRequestURI());
    }

    /**
     * Delete category theo di
     * @param id
     */
    @Override
    public ApiResponse<Void> deleteCategory(Long id, HttpServletRequest request) {
        if (categoryRepository.findById(id).isEmpty()) {
            return ApiResponse.error(null, Constants.CATEGORY_NOT_FOUND,  request.getRequestURI());
        }

        categoryRepository.deleteById(id);
        return ApiResponse.success(null, request.getRequestURI());
    }
}
