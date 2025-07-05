package com.springboot.backend.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto extends BaseDto {

    private String username;

    private String email;

    private String phone;

    private String address;

    private Integer status;

    private Long roleId;

    private Set<String> roles;
}
