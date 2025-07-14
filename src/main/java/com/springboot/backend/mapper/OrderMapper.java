//package com.springboot.backend.mapper;
//
//import com.springboot.backend.dto.OrderDto;
//import com.springboot.backend.entity.Order;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring")
//public interface OrderMapper {
//
//    @Mapping(source = "user.username", target = "name")
//    OrderDto toOrderDto(Order order);
//
//    List<OrderDto> toOrderDtoList(List<Order> orders);
//}
