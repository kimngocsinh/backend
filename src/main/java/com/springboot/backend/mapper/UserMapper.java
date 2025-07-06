//package com.springboot.backend.mapper;
//
//import com.springboot.backend.dto.UserDto;
//import com.springboot.backend.entity.Role;
//import com.springboot.backend.entity.User;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.Named;
//
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Mapper(componentModel = "spring")
//public interface UserMapper {
//
//    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapRolesToNames")
//    UserDto toUserDto(User user);
//
//    List<UserDto> toUserDtoList(List<User> users);
//
//    @Named("mapRolesToNames")
//    default Set<String> toUserDtosSet(Set<Role> roles) {
//        if (roles == null) {
//            return null;
//        }
//        return roles.stream().map(Role::getName).collect(Collectors.toSet());
//    }
//}
