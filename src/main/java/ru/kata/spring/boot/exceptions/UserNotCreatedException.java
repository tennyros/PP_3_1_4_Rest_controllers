package ru.kata.spring.boot.exceptions;

public class UserNotCreatedException extends RuntimeException {

    public UserNotCreatedException() {
        super("User creation failed!");
    }

    public UserNotCreatedException(String message) {
        super(message);
    }
}
