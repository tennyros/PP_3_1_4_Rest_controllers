package ru.kata.spring.boot.exceptions;

public class UserValidationException extends RuntimeException {

    public UserValidationException() {
    }

    public UserValidationException(String message) {
        super(message);
    }
}
