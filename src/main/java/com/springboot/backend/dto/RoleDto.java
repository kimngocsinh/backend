package com.springboot.backend.dto;

import lombok.Data;

@Data
public class RoleDto extends BaseDto {
    private String code;

    private String name;

    private Integer status;

    private String description;
}
