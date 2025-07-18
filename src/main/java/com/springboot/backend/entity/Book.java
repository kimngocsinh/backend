package com.springboot.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

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

    private BigDecimal price;

    private String image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_category_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    @Comment("Số lượng đã mua")
    private Integer purchasedCount;

}
