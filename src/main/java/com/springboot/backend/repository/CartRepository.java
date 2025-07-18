package com.springboot.backend.repository;

import com.springboot.backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long>, JpaSpecificationExecutor<Cart> {
    List<Cart> findByUserIdAndStatus(Long userId, Integer status);
    Optional<Cart> findByUserIdAndBookIdAndStatus(Long userId, Long bookId, Integer status);

}
