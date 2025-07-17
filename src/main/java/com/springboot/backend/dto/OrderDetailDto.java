package com.springboot.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailDto extends BaseDto {

    private Long orderId;

    private String orderName;

    private Long userId;

    private String username;

    private Long bookId;

    private String bookName;

    private Long count;

}
