package com.springboot.backend.dto;

import com.springboot.backend.annotation.UniqueUsername;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;


@Data
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BaseDto {

    @NotBlank(message = "Username is required")
    @Column(unique = true)
    @UniqueUsername
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 24, message = "Password must be between 8 and 24 characters")
    private String password;

    @Email(message = "Email is invalid")
    private String email;

    private String phone;

    private String address;

    private Integer status;

    private Set<String> roles;
}
