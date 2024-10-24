package ru.kata.spring.boot.exceptions;

public class UserNotCreatedException extends BaseException {

    public UserNotCreatedException(String message) {
        super(message);
    }
}
