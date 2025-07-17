package com.springboot.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto extends BaseDto {

    private Long parentId;

    private String name;

    private String code;

    private Set<BookDto> books;
}
