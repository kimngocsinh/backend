package com.springboot.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;


@Data
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BaseDto {

    private String username;

    private String password;

    private String email;

    private String phone;

    private String address;

    private Integer status;

    private Long roleId;

    private Set<String> roles;
}
