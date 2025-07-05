package com.springboot.backend.service.impl;

import com.springboot.backend.dto.BookDto;
import com.springboot.backend.entity.Book;
import com.springboot.backend.repository.BookRepository;
import com.springboot.backend.repository.CategoryRepository;
import com.springboot.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

//    @Autowired
//    private BookMapper bookMapper;

    @Override
    public Page<BookDto> getBooks(int page, int size, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public BookDto getBook(long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("không tìm thay book id = " + id));
        return null;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {

        return null;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {

        return null;
    }

    @Override
    public void deleteBook(long id) {
        if (bookRepository.findById(id).isPresent()) {
            bookRepository.deleteById(id);
        }
    }
}
