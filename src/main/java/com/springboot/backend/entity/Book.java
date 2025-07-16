package com.springboot.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "tbl_book")
public class Book extends BaseEntity{

    private String code;

    private String name;

    private String description;

    @Comment("ngày xuất bản")
    private Date publishDate;

    @Comment("Trạng thái phê duyệt")
    private Integer status;

    private Integer amount;

    private Boolean isDelete;

    private Double price;

    @Column(name = "category_id")
    private Long categoryId;

    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Category category;

    @Comment("Số lượng đã mua")
    private Integer purchasedCount;

}
