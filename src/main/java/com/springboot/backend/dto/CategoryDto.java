package com.springboot.backend.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CategoryDto extends BaseDto {

    private Long parentId;

    private String name;

    private String code;

    private Set<BookDto> books;
}
