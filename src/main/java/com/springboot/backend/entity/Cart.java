package com.springboot.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Table(name = "tbl_cart")
public class Cart extends BaseEntity{

    private Integer quantity;

    private Integer status;

    @Transient
    private BigDecimal totalPrice;

    @Transient
    private Long totalItemCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
