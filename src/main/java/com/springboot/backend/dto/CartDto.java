package com.springboot.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDto extends BaseDto{

    private Long userId;

    private String username;

    private Long bookId;

    private String bookName;

    private BigDecimal bookPrice;

    private Integer quantity;

    private Integer status;

    private BigDecimal totalPrice;
}
