package ru.kata.spring.boot.services;

import ru.kata.spring.boot.dtos.UserRequestDto;
import ru.kata.spring.boot.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void addUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    Optional<User> getUserByEmail(String email);

    void mapAndUpdateRoles(UserRequestDto userRequestDto, User userForUpdate);
    void mapRolesForNewUser(UserRequestDto userRequestDto, User newUser);
}
