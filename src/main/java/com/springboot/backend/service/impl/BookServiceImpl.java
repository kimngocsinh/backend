package com.springboot.backend.service.impl;

import com.springboot.backend.config.Constants;
import com.springboot.backend.dto.BookDto;
import com.springboot.backend.entity.Book;
import com.springboot.backend.entity.Category;
import com.springboot.backend.entity.ResponseDto;
import com.springboot.backend.repository.BookRepository;
import com.springboot.backend.repository.CategoryRepository;
import com.springboot.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public ResponseDto<BookDto> getBook(long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return new ResponseDto<>(false, Constants.BOOK_NOT_FOUND, null);
        }

        Book book = optionalBook.get();
        Optional<Category> categoryOpt = categoryRepository.findById(book.getCategoryId());

        if (categoryOpt.isEmpty()) {
            return new ResponseDto<>(false, Constants.CATEGORY_NOT_FOUND, null);
        }

        BookDto dto = convertEntityToDto(book, categoryOpt);
        return new ResponseDto<>(true, Constants.SUCCESS, dto);
    }

    @Override
    public ResponseDto<BookDto> createBook(BookDto bookDto) {
        // Kiểm tra category có tồn tại không
        Optional<Category> categoryOpt = categoryRepository.findById(bookDto.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return new ResponseDto<>(false, Constants.CATEGORY_NOT_FOUND, null);
        }

        Book book = convertDtoToEntity(bookDto);

        BookDto dto = convertEntityToDto(bookRepository.save(book), categoryOpt);
        return new ResponseDto<>(true, Constants.SUCCESS, dto);
    }

    @Override
    public ResponseDto<BookDto> updateBook(BookDto bookDto) {
        Optional<Book> optionalBook = bookRepository.findById(bookDto.getId());
        if (optionalBook.isEmpty()) {
            return new ResponseDto<>(false, Constants.BOOK_NOT_FOUND, null);
        }
        Book book = optionalBook.get();

        Optional<Category> categoryOpt = categoryRepository.findById(bookDto.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return new ResponseDto<>(false, Constants.CATEGORY_NOT_FOUND, null);
        }

        book = convertDtoToEntity(bookDto);
        BookDto dto = convertEntityToDto(bookRepository.save(book), categoryOpt);

        return new  ResponseDto<>(true, Constants.SUCCESS, dto);
    }

    @Override
    public void deleteBook(long id) {
        if (bookRepository.findById(id).isPresent()) {
            bookRepository.deleteById(id);
        }
    }



    private BookDto convertEntityToDto(Book book, Optional<Category> category) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setCode(book.getCode());
        bookDto.setName(book.getName());
        bookDto.setDescription(book.getDescription());
        bookDto.setPublishDate(book.getPublishDate());
        bookDto.setStatus(book.getStatus());
        bookDto.setAmount(book.getAmount());
        bookDto.setIsDelete(book.getIsDelete());
        bookDto.setPrice(book.getPrice());
        bookDto.setCategoryId(book.getCategoryId());
        bookDto.setImage(book.getImage());
        bookDto.setPurchasedCount(book.getPurchasedCount());

        // Gán thêm thông tin từ Category
        bookDto.setCategoryName(category.get().getName());

        // Audit
        bookDto.setCreatedBy(book.getCreatedBy());
        bookDto.setCreatedDate(book.getCreatedDate());
        bookDto.setModifiedBy(book.getModifiedBy());
        bookDto.setModifiedDate(book.getModifiedDate());

        return bookDto;
    }

    private Book convertDtoToEntity(BookDto bookDto) {
        Book book = new Book();
        book.setCode(bookDto.getCode());
        book.setName(bookDto.getName());
        book.setDescription(bookDto.getDescription());
        book.setPublishDate(bookDto.getPublishDate());
        book.setStatus(bookDto.getStatus());
        book.setAmount(bookDto.getAmount());
        book.setIsDelete(bookDto.getIsDelete());
        book.setPrice(bookDto.getPrice());
        book.setCategoryId(bookDto.getCategoryId());
        book.setImage(bookDto.getImage());
        book.setPurchasedCount(bookDto.getPurchasedCount());

        return  book;
    }
}
