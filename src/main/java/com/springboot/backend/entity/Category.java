package com.springboot.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tbl_category")
public class Category extends BaseEntity{

    private Long parentId;

    private String name;

    private String code;
}
