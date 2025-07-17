package com.springboot.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.backend.entity.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDto extends BaseEntity {

    private String code;

    private String name;

    private String description;

    private Date publishDate;

    private Integer status;

    private Integer amount;

    private Boolean isDelete;

    private Double price;

    private String image;

    private Integer purchasedCount;

    private Set<CategoryDto> categories;
}
