package com.springboot.backend.service.impl;

import com.springboot.backend.config.Constants;
import com.springboot.backend.dto.response.ApiResponse;
import com.springboot.backend.dto.BookDto;
import com.springboot.backend.dto.CategoryDto;
import com.springboot.backend.entity.Book;
import com.springboot.backend.entity.Category;
import com.springboot.backend.repository.BookRepository;
import com.springboot.backend.repository.CategoryRepository;
import com.springboot.backend.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final BookRepository bookRepository;

//    private final CategoryMapper categoryMapper;

    /**
     * Get ban ghi category theo id
     * @param id
     * @return ApiResponse<CategoryDto>
     */
    @Override
    public ApiResponse<CategoryDto> getCategory(Long id, HttpServletRequest request) {
        Category category = categoryRepository.findById(id).orElse(null);

        if (category == null) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), Constants.CATEGORY_NOT_FOUND,  request.getRequestURI());
        }

        List<Book> books = bookRepository.findBooksByCategoryId(category.getId());
        Set<BookDto> bookDtos = books.stream().map(this::createBookDto).collect(Collectors.toSet());

        CategoryDto dto = convertToCategoryDto(category);
        dto.setBooks(bookDtos);

        return ApiResponse.success(dto, request.getRequestURI());
    }

    /**
     * Tao moi category va insert vao DB
     * @param categoryDto
     * @return CategoryDto
     */
    @Override
    public ApiResponse<CategoryDto> createCategory(CategoryDto categoryDto, HttpServletRequest request) {
        Category category = convertToCategory(categoryDto);
        Category saved = categoryRepository.save(category);

        CategoryDto result = convertToCategoryDto(saved);
        return ApiResponse.success(result, request.getRequestURI());
    }

    /**
     * Update category
     * @param categoryDto
     * @return ApiResponse<CategoryDto>
     */
    @Override
    public ApiResponse<CategoryDto> updateCategory(CategoryDto categoryDto, HttpServletRequest request) {
        Optional<Category> optional = categoryRepository.findById(categoryDto.getId());
        if (optional.isEmpty()) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), Constants.CATEGORY_NOT_FOUND,  request.getRequestURI());
        }

        Category category = optional.get();
        category.setName(categoryDto.getName());
        category.setParentId(categoryDto.getParentId());
        category.setCode(categoryDto.getCode());
        Category updated = categoryRepository.save(category);

        CategoryDto result = convertToCategoryDto(updated);
        return ApiResponse.success(result, request.getRequestURI());
    }

    /**
     * Delete category theo di
     * @param id
     */
    @Override
    public ApiResponse<Void> deleteCategory(Long id, HttpServletRequest request) {
        if (categoryRepository.findById(id).isEmpty()) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), Constants.CATEGORY_NOT_FOUND,  request.getRequestURI());
        }

        categoryRepository.deleteById(id);
        return ApiResponse.success(null, request.getRequestURI());
    }

    /**
     * Chuyen tư Entity -> Dto // không gán lại category vì getcategoy lại gán lai category sẽ bị lặp
     * @param book
     * @return BookDto
     */
    private BookDto createBookDto(Book book) {
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
        bookDto.setImage(book.getImage());
        bookDto.setPurchasedCount(book.getPurchasedCount());

        // Audit
        bookDto.setCreatedBy(book.getCreatedBy());
        bookDto.setCreatedDate(book.getCreatedDate());
        bookDto.setModifiedBy(book.getModifiedBy());
        bookDto.setModifiedDate(book.getModifiedDate());
        return bookDto;
    }

    /**
     * Convert entity to Dto
     * @param category
     * @return CategoryDto
     */
    private CategoryDto convertToCategoryDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setCreatedBy(category.getCreatedBy());
        dto.setCreatedDate(category.getCreatedDate());
        dto.setModifiedBy(category.getModifiedBy());
        dto.setModifiedDate(category.getModifiedDate());
        dto.setName(category.getName());
        dto.setParentId(category.getParentId());
        dto.setCode(category.getCode());
        return dto;
    }

    private Category convertToCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setParentId(categoryDto.getParentId());
        category.setCode(categoryDto.getCode());
        return category;
    }
}
