package com.springboot.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "tbl_user")
public class User extends BaseEntity{

    private String username;

    private String password;

    private String email;

    private String phone;

    private String address;

    private Integer status;

    private Long roleId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

}
