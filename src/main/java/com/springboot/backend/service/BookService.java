package com.springboot.backend.service;

import com.springboot.backend.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    public Page<BookDto> getBooks (int page, int size, String sortBy, String sortOrder);

    BookDto getBook (long id);

    BookDto createBook (BookDto bookDto);

    BookDto updateBook (BookDto bookDto);

    void deleteBook (long id);

}
