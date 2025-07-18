package com.springboot.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.backend.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // ẩn các field null
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String status;
    private String path;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(T data, String path) {
        return ApiResponse.<T>builder()
                .success(true)
                .status("200")
                .message(Constants.SUCCESS)
                .path(path)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error (String status, String message, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status)
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
