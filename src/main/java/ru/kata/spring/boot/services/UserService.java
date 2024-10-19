package ru.kata.spring.boot.services;

import ru.kata.spring.boot.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    List<User> getAllUsers();
    Optional<User> getUserByEmail(String email);
}