package com.springboot.backend.dto;

import com.springboot.backend.entity.User;
import lombok.Data;

import java.util.Set;

@Data
public class RoleDto extends BaseDto {
    private String code;

    private String name;

    private Integer status;

    private String description;
}
