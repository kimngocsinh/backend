package com.springboot.backend.service.impl;

import com.springboot.backend.config.Constants;
import com.springboot.backend.dto.CategoryDto;
import com.springboot.backend.entity.Category;
import com.springboot.backend.entity.ResponseDto;
import com.springboot.backend.repository.CategoryRepository;
import com.springboot.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public ResponseDto<CategoryDto> getCategory(Long id) {
        Optional<Category> optional = categoryRepository.findById(id);

        if (optional.isEmpty()) {
            return new ResponseDto<>(false, Constants.CATEGORY_NOT_FOUND, null);
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

        return new ResponseDto<>(true, Constants.SUCCESS, dto);
    }

    /**
     * Tao moi category va insert vao DB
     * @param categoryDto
     * @return CategoryDto
     */
    @Override
    public ResponseDto<CategoryDto> createCategory(CategoryDto categoryDto) {
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
        return new ResponseDto<>(true, Constants.SUCCESS, result);
    }

    /**
     * Update category
     * @param categoryDto
     * @return CategoryDto
     */
    @Override
    public ResponseDto<CategoryDto> updateCategory(CategoryDto categoryDto) {
        Optional<Category> optional = categoryRepository.findById(categoryDto.getId());
        if (optional.isEmpty()) {
            return new ResponseDto<>(false, Constants.CATEGORY_NOT_FOUND, null);
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
        return new ResponseDto<>(true, Constants.SUCCESS, result);
    }

    /**
     * Delete category theo di
     * @param id
     */
    @Override
    public ResponseDto<Void> deleteCategory(Long id) {
        if (categoryRepository.findById(id).isEmpty()) {
            return new ResponseDto<>(false, Constants.CATEGORY_NOT_FOUND, null);
        }

        categoryRepository.deleteById(id);
        return new ResponseDto<>(true, Constants.SUCCESS, null);
    }
}
