package com.springboot.backend.exception;

import com.springboot.backend.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

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

        var optionalFieldError = e.getBindingResult().getFieldErrors().stream().findFirst();

        String errorMessage = optionalFieldError
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .orElse("Validation error");

        if (optionalFieldError.isPresent()) {
            try {
                // Thử unwrap lỗi về ConstraintViolation để lấy attributes
                ConstraintViolation<?> violation = optionalFieldError.get().unwrap(ConstraintViolation.class);
                Map<String, Object> attributes = violation.getConstraintDescriptor().getAttributes(); // trả về map [message = "...", min=3, ...]

                errorMessage = mapAttribute(errorMessage, attributes);
            } catch (Exception ex) {
                // unwrap không thành công thì dùng nguyên message ban đầu
            }
        }

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), errorMessage, request.getRequestURI()));
    }

    /**
     * Custom message: vd tuoi phai tren 18,... dưựa vào giá trị trong annotation
     * @param message
     * @param attributes
     * @return message
     */
    private String mapAttribute (String message, Map<String, Object> attributes) {
        String minValue = attributes.get("min").toString();
        String maxValue = attributes.get("max").toString();
        if (minValue != null) {
            message = message.replace("{min}", minValue);
        }
        if (maxValue != null) {
            message = message.replace("{max}", maxValue);
        }
        return message;
    }
}
