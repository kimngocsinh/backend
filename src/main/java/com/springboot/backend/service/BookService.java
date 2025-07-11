package com.springboot.backend.service;

import com.springboot.backend.dto.ApiResponse;
import com.springboot.backend.dto.BookDto;
import com.springboot.backend.dto.page.BookSearchRequest;
import com.springboot.backend.entity.Book;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface BookService {

    ApiResponse<Page<BookDto>> searchBooks(BookSearchRequest request, HttpServletRequest req);

    ApiResponse<BookDto> getBook (long id, HttpServletRequest request);

    ApiResponse<BookDto> createBook (BookDto bookDto, HttpServletRequest request);

    ApiResponse<BookDto> updateBook (BookDto bookDto, HttpServletRequest request);

    ApiResponse<Void> deleteBook (long id, HttpServletRequest request);
}
