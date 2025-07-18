package com.springboot.backend.exception;

import com.springboot.backend.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Xử lý exception validate
     * @param e
     * @param request
     * @return ResponseEntity<ApiResponse<Void>>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException e,
                                                                        HttpServletRequest request) {
            String errorMessage = e.getBindingResult()
                                    .getFieldErrors()
                                    .stream()
                                    .map(err -> err.getField() + " " + err.getDefaultMessage())
                    .findFirst()
                    .orElse("Error Validation");
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("400", request.getRequestURI(), errorMessage)
            );
    }
}
