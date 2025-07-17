package com.springboot.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDto extends BaseDto{

    private Long userId;

    private String username;      // từ user.username

    private Long bookId;

    private String bookName;     // từ book.name

    private BigDecimal bookPrice;     // từ book.price

    private Integer quantity;

    private Integer status;

    private BigDecimal totalPrice;        // transient

    private Long totalItemCount;
}
