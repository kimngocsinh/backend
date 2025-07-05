package com.springboot.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@Table(name = "tbl_order")
public class Order extends BaseEntity{

    private String name;

    private String code;

    private String description;

    @Comment("Ngày nhận hàng")
    private Date receivedDate;

    @Comment("Ngày giao hàng")
    private Date deliveryDate;

    @Comment("Trạng thái đơn hàng 1.Đã đặt hàng | 2.Đă vận chuyển | 3.Đang giao hàng | 4.Đã giao hàng | 5.Hủy đơn")
    private Integer statusOrder;

    @Comment("Lý do từ chỗi")
    private String reason;

    private Integer status;

    @Comment("Địa chỉ nhận hàng")
    private String receivedAddress;

    private Boolean isDelete;

    @Column(name = "user_id")
    private Long userId;

    @Comment("1.Thanh toán khi nhận hàng || 2 Thanh toán online")
    private Integer typePay;

    @Comment("1.Đã thanh toán || 2.Chưa thanh toán")
    private Integer statusPay;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private User user;
}
