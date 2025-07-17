package com.springboot.backend.controller;

import com.springboot.backend.dto.ApiResponse;
import com.springboot.backend.dto.BookDto;
import com.springboot.backend.dto.page.BookSearchRequest;
import com.springboot.backend.dto.page.PageResult;
import com.springboot.backend.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResult<BookDto>>> searchBooks(@RequestBody BookSearchRequest searchRequest,
                                                                        HttpServletRequest request) {
        return ResponseEntity.ok(bookService.searchBooks(searchRequest, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDto>> getBook(@PathVariable Long id,
                                                        HttpServletRequest request) {
        return ResponseEntity.ok(bookService.getBook(id, request));
    }

    @PostMapping("/insert")
    public ResponseEntity<ApiResponse<BookDto>> createBook(@RequestBody BookDto bookDto,
                                                           HttpServletRequest request) {
        return ResponseEntity.ok(bookService.createBook(bookDto, request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<BookDto>> updateBook(@PathVariable Long id,
                                                           @RequestBody BookDto bookDto,
                                                           HttpServletRequest request) {
        bookDto.setId(id);
        return ResponseEntity.ok(bookService.updateBook(bookDto, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>>  deleteBook(@PathVariable Long id, HttpServletRequest request) {
        bookService.deleteBook(id, request);
        return ResponseEntity.ok(null);
    }
}
