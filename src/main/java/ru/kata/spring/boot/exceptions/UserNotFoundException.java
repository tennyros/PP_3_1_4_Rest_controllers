package ru.kata.spring.boot.exceptions;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
        super("User not found!");
    }
}