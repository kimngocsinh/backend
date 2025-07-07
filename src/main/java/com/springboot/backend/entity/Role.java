package com.springboot.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "tbl_role")
public class Role extends BaseEntity{

    private String code;

    private String name;

    private Integer status;

    private String description;
}
