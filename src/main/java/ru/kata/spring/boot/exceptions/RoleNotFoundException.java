package ru.kata.spring.boot.exceptions;

public class RoleNotFoundException extends BaseException {

    public RoleNotFoundException() {
        super("Role not found!");
    }
}
