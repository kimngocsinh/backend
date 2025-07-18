package com.springboot.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartRequest {

    @NotNull
    private Long bookId;

    @Min(1)
    private Integer quantity;

}
