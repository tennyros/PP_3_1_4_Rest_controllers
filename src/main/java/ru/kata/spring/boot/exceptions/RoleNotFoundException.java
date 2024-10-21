package ru.kata.spring.boot.exceptions;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException() {
        super("There is no such role!");
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}
