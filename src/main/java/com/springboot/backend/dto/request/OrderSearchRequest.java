package com.springboot.backend.dto.request;

import lombok.Data;

@Data
public class OrderSearchRequest {
    String orderName;
    String orderCode;
    String statusOrder;
    String statusPayment;

    private Integer page = 0;
    private Integer pageSize = 10;
    private String sortBy = "id";
    private String sortOrder = "desc";
}
