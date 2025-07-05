package com.springboot.backend.service.impl;

import com.springboot.backend.dto.CategoryDto;
import com.springboot.backend.entity.Category;
import com.springboot.backend.mapper.CategoryMapper;
import com.springboot.backend.repository.CategoryRepository;
import com.springboot.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * Get ban ghi category theo id
     * @param id
     * @return CategoryDto
     */
    @Override
    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElse(null);
        CategoryDto categoryDto = new CategoryDto();
        if (category != null) {
            categoryDto =  categoryMapper.toCategoryDto(category);
        }
        return categoryDto;
    }

    /**
     * Tao moi category va insert vao DB
     * @param categoryDto
     * @return CategoryDto
     */
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        categoryRepository.save(category);
        return categoryMapper.toCategoryDto(category);
    }

    /**
     * Update category
     * @param categoryDto
     * @return CategoryDto
     */
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElse(null);
        if (category != null) {
            categoryMapper.updateCategoryFromDto(categoryDto, category);
            categoryRepository.save(category);
        }
        return categoryDto;
    }

    /**
     * Delete category theo di
     * @param id
     */
    @Override
    public void deleteCategory(Long id) {
        if (categoryRepository.findById(id).isPresent()) {
            categoryRepository.deleteById(id);
        }
    }
}
