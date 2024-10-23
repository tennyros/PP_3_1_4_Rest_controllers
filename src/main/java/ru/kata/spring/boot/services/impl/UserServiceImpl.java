package ru.kata.spring.boot.services.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot.dtos.UserRequestDto;
import ru.kata.spring.boot.exceptions.RoleNotFoundException;
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
    public void addUser(User user) {
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getRoleName()));
        user.setAdmin(isAdmin);
        userRepository.save(user);
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
        userRepository.deleteById(id);
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
    public void mapRolesForNewUser(UserRequestDto userRequestDto, User newUser) {
        Set<String> requestRoleNames = userRequestDto.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        Set<Role> requestedRoles = requestRoleNames.stream()
                .map(roleName -> roleRepository.findByRoleName(roleName)
                        .orElseThrow(RoleNotFoundException::new))
                .collect(Collectors.toSet());

        newUser.setRoles(requestedRoles);
    }

    public void mapAndUpdateRoles(UserRequestDto userRequestDto, User userFroUpdate) {
        Set<Role> currentRoles = userRepository.getById(userRequestDto.getId()).getRoles();

        Set<String> requestedRoleNames = userRequestDto.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        Set<Role> requestedRoles = requestedRoleNames.stream()
                .map(roleName -> roleRepository.findByRoleName(roleName)
                        .orElseThrow(RoleNotFoundException::new))
                .collect(Collectors.toSet());

        Set<Role> rolesToAdd = requestedRoles.stream()
                .filter(role -> !currentRoles.contains(role))
                .collect(Collectors.toSet());

        Set<Role> rolesToRemove = currentRoles.stream()
                .filter(role -> !requestedRoles.contains(role))
                .collect(Collectors.toSet());

        currentRoles.addAll(rolesToAdd);
        currentRoles.removeAll(rolesToRemove);
        userRequestDto.setRoles(currentRoles);
    }
}
