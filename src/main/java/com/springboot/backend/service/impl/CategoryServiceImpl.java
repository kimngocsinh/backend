package com.springboot.backend.service.impl;

import com.springboot.backend.config.Constants;
import com.springboot.backend.dto.ApiResponse;
import com.springboot.backend.dto.BookDto;
import com.springboot.backend.dto.CategoryDto;
import com.springboot.backend.entity.Book;
import com.springboot.backend.entity.Category;
import com.springboot.backend.repository.BookRepository;
import com.springboot.backend.repository.CategoryRepository;
import com.springboot.backend.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final BookRepository bookRepository;

//    private final CategoryMapper categoryMapper;

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
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setCreatedBy(category.getCreatedBy());
        dto.setCreatedDate(category.getCreatedDate());
        dto.setModifiedBy(category.getModifiedBy());
        dto.setModifiedDate(category.getModifiedDate());
        dto.setName(category.getName());
        dto.setParentId(category.getParentId());
        dto.setCode(category.getCode());

        return ApiResponse.success(dto, request.getRequestURI());
    }

    /**
     * Tao moi category va insert vao DB
     * @param categoryDto
     * @return CategoryDto
     */
    @Override
    public ApiResponse<CategoryDto> createCategory(CategoryDto categoryDto, HttpServletRequest request) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setParentId(categoryDto.getParentId());
        category.setCode(categoryDto.getCode());
        Category saved = categoryRepository.save(category);

        CategoryDto result = new CategoryDto();
        result.setId(saved.getId());
        result.setName(saved.getName());
        result.setParentId(saved.getParentId());
        result.setCode(saved.getCode());
        result.setCreatedBy(saved.getCreatedBy());
        result.setCreatedDate(saved.getCreatedDate());
        result.setModifiedBy(saved.getModifiedBy());
        result.setModifiedDate(saved.getModifiedDate());
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
        CategoryDto result = new CategoryDto();
        Category updated = categoryRepository.save(category);

        result.setId(updated.getId());
        result.setName(updated.getName());
        result.setCode(updated.getCode());
        result.setParentId(updated.getParentId());
        result.setModifiedBy(updated.getModifiedBy());
        result.setModifiedDate(updated.getModifiedDate());
        result.setCreatedBy(updated.getCreatedBy());
        result.setCreatedDate(updated.getCreatedDate());
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
