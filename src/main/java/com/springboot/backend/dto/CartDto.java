package com.springboot.backend.dto;

import lombok.Data;

@Data
public class CartDto extends BaseDto{

    private Long userId;

    private String username;      // từ user.username

    private Long bookId;

    private String bookName;     // từ book.name

    private Double bookPrice;     // từ book.price

    private Integer quantity;

    private Integer status;

    private Integer price;        // transient

    private Long count;
}
