package com.springboot.backend.service;

import com.springboot.backend.dto.response.ApiResponse;
import com.springboot.backend.dto.OrderDto;
import com.springboot.backend.dto.response.PageResult;
import jakarta.servlet.http.HttpServletRequest;

public interface OrderService {
    ApiResponse<PageResult<OrderDto>> searchOrders(OrderDto request, HttpServletRequest req);
}
