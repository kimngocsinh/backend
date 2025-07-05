package com.springboot.backend.dto;

import lombok.Data;

@Data
public class OrderDetailDto extends BaseDto {

    private Long orderId;

    private String orderName;

    private Long userId;

    private String username;

    private Long bookId;

    private String bookName;

    private Long count;

}
