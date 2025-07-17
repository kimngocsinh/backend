package com.springboot.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.backend.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto extends BaseEntity {

    private String name;

    private String code;

    private String description;

    private Date receivedDate;

    private Date deliveryDate;

    private Integer statusOrder;

    private String reason;

    private Integer status;

    private String receivedAddress;

    private Boolean isDelete;

    private Long userId;

    private String username;  // từ User liên kết

    private Integer typePay;

    private Integer statusPayment;

}
