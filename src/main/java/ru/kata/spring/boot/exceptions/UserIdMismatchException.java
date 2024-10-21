package ru.kata.spring.boot.exceptions;

public class UserIdMismatchException extends RuntimeException {

    public UserIdMismatchException() {
        super("User ID in the request does not match the ID in the body!");
    }

    public UserIdMismatchException(String message) {
        super(message);
    }
}
