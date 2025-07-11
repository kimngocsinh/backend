package com.springboot.backend.dto.page;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationInfo {
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPages;
    private Integer totalItems;
    private boolean isFirstPage;
    private boolean isLastPage;
    private String sortBy;
    private String sortOrder;
}
