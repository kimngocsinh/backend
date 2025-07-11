package com.springboot.backend.mapper;

import com.springboot.backend.dto.CartDto;
import com.springboot.backend.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "book.name", target = "bookName")
    CartDto toCartDto(Cart cart);

    List<CartDto> toCartDtoList(List<Cart> carts);
}
