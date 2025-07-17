package com.springboot.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "tbl_category")
public class Category extends BaseEntity{

    private Long parentId;

    private String name;

    private String code;

    @ManyToMany(mappedBy = "categories")
    private Set<Book> books = new HashSet<>();
}
