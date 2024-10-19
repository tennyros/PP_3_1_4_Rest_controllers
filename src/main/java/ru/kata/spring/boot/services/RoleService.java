package ru.kata.spring.boot.services;

import ru.kata.spring.boot.models.Role;

import java.util.Set;

public interface RoleService {
    void addRole(Role role);
    Role getRoleByName(String roleName);
    Set<Role> getAllRoles();
}