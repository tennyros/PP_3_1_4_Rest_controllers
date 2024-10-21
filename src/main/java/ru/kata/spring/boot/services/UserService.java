package ru.kata.spring.boot.services;

import ru.kata.spring.boot.dtos.UserRequestDto;
import ru.kata.spring.boot.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User addUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    Optional<User> getUserByEmail(String email);
    User mapAndSetRoles(UserRequestDto userRequestDto, User userForSaving);
}
