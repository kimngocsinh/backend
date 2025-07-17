package com.springboot.backend.service.impl;

import com.springboot.backend.config.Constants;
import com.springboot.backend.dto.ApiResponse;
import com.springboot.backend.dto.BookDto;
import com.springboot.backend.dto.CategoryDto;
import com.springboot.backend.dto.page.BookSearchRequest;
import com.springboot.backend.dto.page.PageResult;
import com.springboot.backend.dto.page.PaginationInfo;
import com.springboot.backend.entity.Book;
import com.springboot.backend.entity.Category;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final CategoryRepository categoryRepository;

//    private final BookMapper bookMapper;

    /**
     * Thực hiện search và phân trang book
     * @param request
     * @param req
     * @return ApiResponse<Page<BookDto>>
     */
    @Override
    public ApiResponse<PageResult<BookDto>> searchBooks(BookSearchRequest request, HttpServletRequest req) {
        String sortBy = request.getSortBy();
        Sort.Direction sortOrder = Sort.Direction.fromOptionalString(request.getSortOrder()).orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize(), Sort.by(sortOrder, sortBy));

        Specification<Book> spec = build(request);

        Page<Book> page = bookRepository.findAll(spec, pageable);
        Page<BookDto> bookDtoPage = page.map(book -> {
           BookDto  bookDto = new BookDto();
           convertEntityToDto(book, bookDto);
           return bookDto;
        });

        PaginationInfo pageInfo = PaginationInfo.builder()
                .currentPage(bookDtoPage.getNumber())
                .pageSize(bookDtoPage.getSize())
                .totalPages(bookDtoPage.getTotalPages())
                .totalItems((int)bookDtoPage.getTotalElements())
                .isFirstPage(bookDtoPage.isFirst())
                .isLastPage(bookDtoPage.isLast())
                .sortBy(sortBy)
                .sortOrder(sortOrder.name())
                .build();

        PageResult<BookDto> result = PageResult.<BookDto>builder()
                .list(bookDtoPage.getContent())
                .paginationInfo(pageInfo)
                .build();

        return ApiResponse.success(result, req.getRequestURI());
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
        List<Long> categoryIds = book.getCategories().stream().map(Category::getId).toList();
        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if (categories.isEmpty() || categories.size() != categoryIds.size()) {
            return ApiResponse.error(null, Constants.CATEGORY_NOT_FOUND,  request.getRequestURI());
        }
        BookDto bookDto = new BookDto();
        convertEntityToDto(book, bookDto);
        Set<CategoryDto> categoryDtos = categories.stream().map(this::createCategoryDto).collect(Collectors.toSet());
        bookDto.setCategories(categoryDtos);
        return ApiResponse.success(bookDto, request.getRequestURI());
    }

    /**
     * Create new book
     * @param bookDto
     * @return ApiResponse<BookDto>
     */
    @Override
    public ApiResponse<BookDto> createBook(BookDto bookDto, HttpServletRequest request) {
        List<Long> categoryIds = bookDto.getCategories().stream().map(CategoryDto::getId).toList();
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        if (categories.isEmpty() ||  categories.size() != categoryIds.size()) {
            return ApiResponse.error(null, Constants.CATEGORY_NOT_FOUND,  request.getRequestURI());
        }

        Book book = new Book();
        convertDtoToEntity(bookDto, book);
        book.setCategories(new HashSet<>(categories));

        Book savedBook = bookRepository.save(book);

        BookDto resultDto = new BookDto();
        convertEntityToDto(savedBook, resultDto);

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

        List<Long> categoryIds = bookDto.getCategories().stream().map(CategoryDto::getId).toList();
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        if (categoryIds.isEmpty() || categories.size() != categoryIds.size()) {
            return ApiResponse.error(null, Constants.CATEGORY_NOT_FOUND,  request.getRequestURI());
        }
        Set<CategoryDto> categoryDtos = categories.stream().map(this::createCategoryDto).collect(Collectors.toSet());

        convertDtoToEntity(bookDto, book);
        bookDto.setCategories(categoryDtos);
        convertEntityToDto(bookRepository.save(book), bookDto);

        return ApiResponse.success(bookDto, request.getRequestURI());
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

    /**
     * Convert Entity to Dto
     * @param book
     * @return BookDto
     */
    private void convertEntityToDto(Book book, BookDto bookDto) {
        bookDto.setId(book.getId());
        bookDto.setCode(book.getCode());
        bookDto.setName(book.getName());
        bookDto.setDescription(book.getDescription());
        bookDto.setPublishDate(book.getPublishDate());
        bookDto.setStatus(book.getStatus());
        bookDto.setAmount(book.getAmount());
        bookDto.setIsDelete(book.getIsDelete());
        bookDto.setPrice(book.getPrice());
        bookDto.setImage(book.getImage());
        bookDto.setPurchasedCount(book.getPurchasedCount());

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
        book.setImage(bookDto.getImage());
        book.setPurchasedCount(bookDto.getPurchasedCount());
    }

    /**
     * create categoryDto
     * @param category
     * @return CategoryDto
     */
    private CategoryDto createCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setParentId(category.getParentId());
        categoryDto.setModifiedBy(category.getModifiedBy());
        categoryDto.setModifiedDate(category.getModifiedDate());
        categoryDto.setCreatedBy(category.getCreatedBy());
        categoryDto.setCreatedDate(category.getCreatedDate());
        return categoryDto;
    }
}
