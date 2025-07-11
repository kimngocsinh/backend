package com.springboot.backend.mapper;

import com.springboot.backend.dto.CategoryDto;
import com.springboot.backend.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toCategoryDto(Category category);

    Category toCategory(CategoryDto categoryDto);

    void updateCategoryFromDto(CategoryDto categoryDto, @MappingTarget Category category);

    List<CategoryDto> toCategoryDtoList(List<Category> categories);
}
