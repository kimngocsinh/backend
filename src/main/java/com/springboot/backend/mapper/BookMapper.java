//package com.springboot.backend.mapper;
//
//import com.springboot.backend.dto.BookDto;
//import com.springboot.backend.entity.Book;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.MappingTarget;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring")
//public interface BookMapper {
//
//    // Book -> DTO: lấy category.name
//    @Mapping(source = "category.name", target = "categoryName")
//    BookDto toBookDto(Book book);
//
//    // DTO -> Book: bỏ qua category (set bằng tay ở Service)
//    @Mapping(target = "category", ignore = true)
//    Book toBook(BookDto dto);
//
//    @Mapping(target = "category", ignore = true)
//    void updateBookFromDto(BookDto dto, @MappingTarget Book book);
//
//    List<BookDto> toBookDtoList(List<Book> books);
//}
