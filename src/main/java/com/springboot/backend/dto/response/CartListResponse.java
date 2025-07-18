package com.springboot.backend.dto.response;

import com.springboot.backend.dto.CartDto;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@RequiredArgsConstructor
@Builder
public class CartListResponse {
    private List<CartDto> items;
    private Integer totalItemCount;
    private BigDecimal grandTotal;
}
