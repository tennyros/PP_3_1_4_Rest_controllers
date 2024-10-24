package ru.kata.spring.boot.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BaseException extends RuntimeException{

    private final LocalDateTime timestamp;

    public BaseException(String message) {
        super(message);
        this.timestamp = LocalDateTime.now();
    }
}
