package ru.kata.spring.boot.services.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot.dtos.UserRequestDto;
import ru.kata.spring.boot.exceptions.RoleNotFoundException;
import ru.kata.spring.boot.exceptions.UserNotFoundException;
import ru.kata.spring.boot.models.Role;
import ru.kata.spring.boot.repositories.RoleRepository;
import ru.kata.spring.boot.repositories.UserRepository;
import ru.kata.spring.boot.models.User;
import ru.kata.spring.boot.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User addUser(User user) {
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getRoleName()));
        user.setAdmin(isAdmin);
        return userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getRoleName()));
        user.setAdmin(isAdmin);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (id == 1) {
            log.error("You are trying to delete a user with id {} (super administrator)!", id);
            throw new UnsupportedOperationException("You can not delete super administrator!");
        }
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAllUsersWithRole();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User mapAndSetRoles(UserRequestDto userRequestDto, User userForSaving) {
        Set<Role> roles = userRequestDto.getRoles().stream()
                .map(roleDto -> roleRepository.findByRoleName(roleDto.getRoleName())
                        .orElseThrow(RoleNotFoundException::new))
                .collect(Collectors.toSet());
        userForSaving.setRoles(roles);
        return userForSaving;
    }
}
