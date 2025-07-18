package com.springboot.backend.dto.request;

import lombok.Data;

@Data
public class BookSearchRequest {
    private String bookName;
    private String categoryName;
    private Integer status;
    private Double minPrice;
    private Double maxPrice;

    private Integer page = 0;
    private Integer pageSize = 10;
    private String sortBy = "id";
    private String sortOrder = "desc";
}
