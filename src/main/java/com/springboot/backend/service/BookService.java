package com.springboot.backend.service;

import com.springboot.backend.dto.response.ApiResponse;
import com.springboot.backend.dto.BookDto;
import com.springboot.backend.dto.request.BookSearchRequest;
import com.springboot.backend.dto.response.PageResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface BookService {

    ApiResponse<PageResult<BookDto>> searchBooks(BookSearchRequest request, HttpServletRequest req);

    ApiResponse<BookDto> getBook (long id, HttpServletRequest request);

    ApiResponse<BookDto> createBook (BookDto bookDto, HttpServletRequest request);

    ApiResponse<BookDto> updateBook (BookDto bookDto, HttpServletRequest request);

    ApiResponse<Void> deleteBook (long id, HttpServletRequest request);
}
