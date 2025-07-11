package com.springboot.backend.mapper;

import com.springboot.backend.dto.OrderDetailDto;
import com.springboot.backend.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "book.name", target = "bookName")
    @Mapping(source = "order.name", target = "orderName")
    OrderDetailDto toOrderDetailDto(OrderDetail orderDetail);

    List<OrderDetailDto> toOrderDetailDtoList(List<OrderDetail> orderDetails);
}
