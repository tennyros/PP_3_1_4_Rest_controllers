package ru.kata.spring.boot.exceptions;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException() {
        super("Role with such name not found!");
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}
