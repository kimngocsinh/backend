package com.springboot.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "tbl_order")
public class Order extends BaseEntity{

    private String name;

    private String code;

    private String description;

    @Comment("Ngày nhận hàng")
    private LocalDate receivedDate;

    @Comment("Ngày giao hàng")
    private LocalDate deliveryDate;

    @Comment("Trạng thái đơn hàng 1.Đã đặt hàng | 2.Đă vận chuyển | 3.Đang giao hàng | 4.Đã giao hàng | 5.Hủy đơn")
    private Integer statusOrder;

    @Comment("Lý do từ chỗi")
    private String reason;

    private Integer status;

    @Comment("Địa chỉ nhận hàng")
    private String receivedAddress;

    private Boolean isDelete;

    @Comment("1.Thanh toán khi nhận hàng || 2 Thanh toán online")
    private Integer typePay;

    @Comment("1.Đã thanh toán || 2.Chưa thanh toán")
    private Integer statusPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
