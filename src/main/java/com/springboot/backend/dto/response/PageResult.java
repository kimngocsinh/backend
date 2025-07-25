package com.springboot.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResult <T>{
    private List<T> list;
    private PaginationInfo paginationInfo;
}
