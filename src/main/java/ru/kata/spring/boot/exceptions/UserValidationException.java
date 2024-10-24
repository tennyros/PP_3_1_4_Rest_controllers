package ru.kata.spring.boot.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class UserValidationException extends BaseException {

    private final Map<String, String> fieldErrors;

    public UserValidationException(Map<String, String> fieldErrors) {
        super("User validation failed!");
        this.fieldErrors = fieldErrors;
    }
}
