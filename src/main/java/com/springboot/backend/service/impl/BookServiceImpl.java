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

    /**
     * get book theo id
     * @param id
     * @return ResponseDto<BookDto>
     */
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
        BookDto bookDto = new BookDto();
        convertEntityToDto(book, categoryOpt.get(), bookDto);
        return new ResponseDto<>(true, Constants.SUCCESS, bookDto);
    }

    /**
     * Create new book
     * @param bookDto
     * @return ResponseDto<BookDto>
     */
    @Override
    public ResponseDto<BookDto> createBook(BookDto bookDto) {
        Optional<Category> categoryOpt = categoryRepository.findById(bookDto.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return new ResponseDto<>(false, Constants.CATEGORY_NOT_FOUND, null);
        }

        Book book = new Book();
        convertDtoToEntity(bookDto, book);

        Book savedBook = bookRepository.save(book);

        BookDto resultDto = new BookDto();
        convertEntityToDto(savedBook, categoryOpt.get(), resultDto);

        return new ResponseDto<>(true, Constants.SUCCESS, resultDto);
    }

    /**
     * Update Book
     * @param bookDto
     * @return ResponseDto<BookDto>
     */
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

        convertDtoToEntity(bookDto, book);
        convertEntityToDto(bookRepository.save(book), categoryOpt.get(), bookDto);

        return new  ResponseDto<>(true, Constants.SUCCESS, bookDto);
    }

    /**
     * Delete Book theo id
     * @param id
     * @return ResponseDto<BookDto>
     */
    @Override
    public ResponseDto<Void> deleteBook(long id) {
        if (bookRepository.findById(id).isEmpty()) {
            return new ResponseDto<>(false, Constants.BOOK_NOT_FOUND, null);
        }

        bookRepository.deleteById(id);
        return new ResponseDto<>(true, Constants.SUCCESS, null);
    }


    /**
     * Convert Entity to Dto
     * @param book
     * @param category
     * @return BookDto
     */
    private void convertEntityToDto(Book book, Category category, BookDto bookDto) {
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
        bookDto.setCategoryName(category.getName());

        // Audit
        bookDto.setCreatedBy(book.getCreatedBy());
        bookDto.setCreatedDate(book.getCreatedDate());
        bookDto.setModifiedBy(book.getModifiedBy());
        bookDto.setModifiedDate(book.getModifiedDate());
    }

    /**
     * conver Dto to Entity
     * @param bookDto
     * @return Book
     */
    private void convertDtoToEntity(BookDto bookDto, Book book) {
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
    }
}
