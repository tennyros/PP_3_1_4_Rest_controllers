package ru.kata.spring.boot.exceptions;

public class    UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("No such user found with given ID!");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}