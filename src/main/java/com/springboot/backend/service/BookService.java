package com.springboot.backend.service;

import com.springboot.backend.dto.BookDto;
import com.springboot.backend.entity.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    public Page<BookDto> getBooks (int page, int size, String sortBy, String sortOrder);

    ResponseDto<BookDto> getBook (long id);

    ResponseDto<BookDto> createBook (BookDto bookDto);

    ResponseDto<BookDto> updateBook (BookDto bookDto);

    ResponseDto<Void> deleteBook (long id);

}
