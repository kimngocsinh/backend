package com.springboot.backend.dto.response;

import com.springboot.backend.dto.CartDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartListResponse {
    private List<CartDto> items;
    private Integer totalItemCount;
    private BigDecimal grandTotal;
}
