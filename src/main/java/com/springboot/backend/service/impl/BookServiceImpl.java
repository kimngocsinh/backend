package com.springboot.backend.service.impl;

import com.springboot.backend.config.Constants;
import com.springboot.backend.dto.ApiResponse;
import com.springboot.backend.dto.BookDto;
import com.springboot.backend.dto.page.BookSearchRequest;
import com.springboot.backend.entity.Book;
import com.springboot.backend.entity.Category;
import com.springboot.backend.mapper.BookMapper;
import com.springboot.backend.repository.BookRepository;
import com.springboot.backend.repository.CategoryRepository;
import com.springboot.backend.service.BookService;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final CategoryRepository categoryRepository;

    private final BookMapper bookMapper;

    /**
     * Thực hiện search và phân trang book
     * @param request
     * @param req
     * @return ApiResponse<Page<BookDto>>
     */
    @Override
    public ApiResponse<Page<BookDto>> searchBooks(BookSearchRequest request, HttpServletRequest req) {
        String sortBy = request.getSortBy();
        Sort.Direction sortOrder = Sort.Direction.fromOptionalString(request.getSortOrder()).orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize(), Sort.by(sortOrder, sortBy));

        Specification<Book> spec = build(request);

        Page<Book> page = bookRepository.findAll(spec, pageable);
        Page<BookDto> bookDtoPage = page.map(bookMapper::toBookDto);
        return ApiResponse.success(bookDtoPage, req.getRequestURI());
    }

    /**
     * get book theo id
     * @param id
     * @return ApiResponse<BookDto>
     */
    @Override
    public ApiResponse<BookDto> getBook(long id, HttpServletRequest request) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return ApiResponse.error(null, Constants.BOOK_NOT_FOUND,  request.getRequestURI());
        }

        Book book = optionalBook.get();
        Optional<Category> categoryOpt = categoryRepository.findById(book.getCategoryId());

        if (categoryOpt.isEmpty()) {
            return ApiResponse.error(null, Constants.CATEGORY_NOT_FOUND,  request.getRequestURI());
        }
        BookDto bookDto = bookMapper.toBookDto(book);
        bookDto.setCategoryId(book.getCategory().getId());
        return ApiResponse.success(bookDto, request.getRequestURI());
    }

    /**
     * Create new book
     * @param bookDto
     * @return ApiResponse<BookDto>
     */
    @Override
    public ApiResponse<BookDto> createBook(BookDto bookDto, HttpServletRequest request) {
        Optional<Category> categoryOpt = categoryRepository.findById(bookDto.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return ApiResponse.error(null, Constants.CATEGORY_NOT_FOUND,  request.getRequestURI());
        }

        Book book = bookMapper.toBook(bookDto);
        Book savedBook = bookRepository.save(book);
        BookDto resultDto = bookMapper.toBookDto(savedBook);

        return ApiResponse.success(resultDto, request.getRequestURI());
    }

    /**
     * Update Book
     * @param bookDto
     * @return ApiResponse<BookDto>
     */
    @Override
    public ApiResponse<BookDto> updateBook(BookDto bookDto, HttpServletRequest request) {
        Optional<Book> optionalBook = bookRepository.findById(bookDto.getId());
        if (optionalBook.isEmpty()) {
            return ApiResponse.error(null, Constants.BOOK_NOT_FOUND,  request.getRequestURI());
        }
        Book book = optionalBook.get();

        Optional<Category> categoryOpt = categoryRepository.findById(bookDto.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return ApiResponse.error(null, Constants.CATEGORY_NOT_FOUND,  request.getRequestURI());
        }

        bookMapper.updateBookFromDto(bookDto, book);
        book.setCategory(categoryOpt.get());

        Book savedBook = bookRepository.save(book);
        BookDto resultDto = bookMapper.toBookDto(savedBook);

        return ApiResponse.success(resultDto, request.getRequestURI());
    }

    /**
     * Delete Book theo id
     * @param id
     * @return ResponseDto<BookDto>
     */
    @Override
    public ApiResponse<Void> deleteBook(long id, HttpServletRequest  request) {
        if (bookRepository.findById(id).isEmpty()) {
            return ApiResponse.error(null, Constants.BOOK_NOT_FOUND,  request.getRequestURI());
        }

        bookRepository.deleteById(id);
        return ApiResponse.success(null, request.getRequestURI());
    }

    /**
     * Thực hiện search theo các điều kiện
     * @param req
     * @return Specification<Book>
     */
    private Specification<Book> build (BookSearchRequest req) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(req.getBookName())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("bookName")), "%" + req.getBookName() + "%"));
            }

            if (StringUtils.hasText(req.getCategoryName())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("categoryName")), "%" + req.getCategoryName() + "%"));
            }

            if (req.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), req.getStatus()));
            }

            if (req.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), req.getMinPrice()));
            }

            if (req.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), req.getMaxPrice()));
            }

//            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
