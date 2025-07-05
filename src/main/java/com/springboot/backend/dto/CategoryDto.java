package com.springboot.backend.dto;

import lombok.Data;

@Data
public class CategoryDto extends BaseDto {

    private Long parentId;

    private String name;

    private String code;
}
