package com.springboot.backend.service.impl;

import com.springboot.backend.dto.CategoryDto;
import com.springboot.backend.entity.Category;
import com.springboot.backend.repository.CategoryRepository;
import com.springboot.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

//    @Autowired
//    private CategoryMapper categoryMapper;

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
            categoryDto.setId(category.getId());
            categoryDto.setCreatedBy(category.getCreatedBy());
            categoryDto.setCreatedDate(category.getCreatedDate());
            categoryDto.setModifiedBy(category.getModifiedBy());
            categoryDto.setModifiedDate(category.getModifiedDate());
            categoryDto.setName(category.getName());
            categoryDto.setParentId(category.getParentId());
            categoryDto.setCode(category.getCode());
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
        return categoryDto;
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
        CategoryDto result = new CategoryDto();
        if (category != null) {
            Category updated = categoryRepository.save(category);

            result.setId(updated.getId());
            result.setName(updated.getName());
            result.setCode(updated.getCode());
            result.setParentId(updated.getParentId());
            result.setModifiedBy(updated.getModifiedBy());
            result.setModifiedDate(updated.getModifiedDate());
            result.setCreatedBy(updated.getCreatedBy());
            result.setCreatedDate(updated.getCreatedDate());
        }
        return result;
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
