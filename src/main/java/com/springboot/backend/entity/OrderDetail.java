package com.springboot.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Table(name = "tbl_order_detail")
@EqualsAndHashCode(of = "bookId", callSuper = false) // ghi de hashcode de so sanh 2 orderDetail theo id
public class OrderDetail extends BaseEntity {

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "book_id")
    private Long bookId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", referencedColumnName = "id",  insertable = false, updatable = false, nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Order order;

    @Transient
    private Long count;


}
