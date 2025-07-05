package com.springboot.backend.dto;

import com.springboot.backend.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class BookDto extends BaseEntity {

    private String code;

    private String name;

    private String description;

    private Date publishDate;

    private Integer status;

    private Integer amount;

    private Boolean isDelete;

    private Double price;

    private Long categoryId;

    private String categoryName;  // từ quan hệ Category

    private String image;

    private Integer purchasedCount;
}
