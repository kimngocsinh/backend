package com.springboot.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
@Table(name = "tbl_role")
public class Role extends BaseEntity{

    private String code;

    private String name;

    private Integer status;

    private String description;
}
