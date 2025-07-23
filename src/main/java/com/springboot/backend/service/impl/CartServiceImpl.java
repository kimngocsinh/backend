package com.springboot.backend.service.impl;

import com.springboot.backend.config.Constants;
import com.springboot.backend.dto.CartDto;
import com.springboot.backend.dto.request.CartRequest;
import com.springboot.backend.dto.response.ApiResponse;
import com.springboot.backend.dto.response.CartListResponse;
import com.springboot.backend.entity.Book;
import com.springboot.backend.entity.Cart;
import com.springboot.backend.entity.User;
import com.springboot.backend.repository.BookRepository;
import com.springboot.backend.repository.CartRepository;
import com.springboot.backend.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    /**
     * Hiển thị cart theo user dang login và status =1 chưa thanh toán
     * @param req
     * @return ApiResponse<CartListResponse>
     */
    @Override
    public ApiResponse<CartListResponse> getAllCarts(HttpServletRequest req) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), Constants.USER_NOT_LOGIN, req.getRequestURI());
        }
        User user = (User) authentication.getPrincipal();
        List<Cart> carts = cartRepository.findByUserIdAndStatus(user.getId(), 1);

        List<CartDto> cartDtos = carts.stream().map(cart -> {
            CartDto cartDto = new CartDto();
            cartDto.setId(cart.getId());
            cartDto.setUserId(user.getId());
            cartDto.setUsername(user.getUsername());
            if (cart.getBook() != null) {
                cartDto.setBookId(cart.getBook().getId());
                cartDto.setBookName(cart.getBook().getName());
                cartDto.setBookPrice(cart.getBook().getPrice());
                cartDto.setQuantity(cart.getQuantity());
            }
            cartDto.setStatus(cart.getStatus());

            BigDecimal totalPrice = cart.getBook().getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()));
            cartDto.setTotalPrice(totalPrice);
            return cartDto;
        }).toList();

        BigDecimal totalPriceAllItem = cartDtos.stream()
                .map(CartDto::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartListResponse cartListResponse = new CartListResponse();
        cartListResponse.setItems(cartDtos);
        cartListResponse.setTotalItemCount(cartDtos.size());
        cartListResponse.setGrandTotal(totalPriceAllItem);

        return ApiResponse.success(cartListResponse, req.getRequestURI());
    }

    /**
     * Xoá sản phẩm trong cart
     * @param id
     * @param req
     * @return
     */
    @Override
    public ApiResponse<Void> deleteCart(Long id, HttpServletRequest req) {
        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart == null) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), Constants.CART_NOT_FOUND,  req.getRequestURI());
        }
        cartRepository.delete(cart);
        return ApiResponse.success(null, req.getRequestURI());
    }

    /**
     * add to cart
     * @param cartRequest
     * @param req
     * @return ApiResponse<CartDto>
     */
    @Override
    public ApiResponse<CartDto> addToCart(CartRequest cartRequest, HttpServletRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), Constants.USER_NOT_LOGIN, req.getRequestURI());
        }
        User user = (User) authentication.getPrincipal();
        Book book = bookRepository.findById(cartRequest.getBookId()).orElse(null);
        if (book == null) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), Constants.BOOK_NOT_FOUND,  req.getRequestURI());
        }
        Cart cart = cartRepository.findByUserIdAndBookIdAndStatus(user.getId(), book.getId(), 1).orElse(null);
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
        } else {
            cart = new Cart();
            cart.setUser(user);
            cart.setBook(book);
            cart.setQuantity(cartRequest.getQuantity());
            cart.setStatus(1);
            cartRepository.save(cart);
        }
        CartDto cartDto = getCartDto(cart, user, book);
        return ApiResponse.success(cartDto, req.getRequestURI());
    }

    /**
     * Get cartDto
     * @param cart
     * @param user
     * @param book
     * @return CartDto
     */
    private CartDto getCartDto(Cart cart, User user, Book book) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setUserId(user.getId());
        cartDto.setUsername(user.getUsername());
        cartDto.setBookId(book.getId());
        cartDto.setBookName(book.getName());
        cartDto.setBookPrice(cart.getBook().getPrice());
        cartDto.setQuantity(cart.getQuantity());
        cartDto.setStatus(cart.getStatus());
        cartDto.setTotalPrice(cart.getBook().getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
        cartDto.setCreatedBy(cart.getUser().getUsername());
        cartDto.setCreatedDate(cart.getCreatedDate());
        cartDto.setModifiedBy(cart.getUser().getUsername());
        cartDto.setModifiedDate(cart.getModifiedDate());
        return cartDto;
    }
}
