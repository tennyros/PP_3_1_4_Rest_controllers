package ru.kata.spring.boot.http.handlers;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class UserErrorResponse {

    private Map<String, String> fieldErrors;
    private String error;
    private LocalDateTime timestamp;

    public UserErrorResponse(Map<String, String> fieldErrors, LocalDateTime timestamp) {
        this.fieldErrors = fieldErrors;
        this.timestamp = timestamp;
    }

    public UserErrorResponse(String error, LocalDateTime timestamp) {
        this.error = error;
        this.timestamp = timestamp;
    }
}
