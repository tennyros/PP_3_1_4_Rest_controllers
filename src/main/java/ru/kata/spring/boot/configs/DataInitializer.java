package ru.kata.spring.boot.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot.exceptions.RoleNotFoundException;
import ru.kata.spring.boot.models.Role;
import ru.kata.spring.boot.models.User;
import ru.kata.spring.boot.services.RoleService;
import ru.kata.spring.boot.services.UserService;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Value("${admin.first_name}")
    private String adminFirstName;

    @Value("${admin.last_name}")
    private String adminLastName;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.age}")
    private Integer adminAge;

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        createRoles("ROLE_USER");
        createRoles("ROLE_ADMIN");
        createAdmin(adminFirstName, adminLastName, adminPassword, adminEmail, adminAge);
    }

    private void createRoles(String roleName) {
        if (roleService.getRoleByName(roleName).isEmpty()) {
            roleService.addRole(new Role(roleName));
        }
    }

    private void createAdmin(String firstName, String lastName, String password, String email, Integer age) {
        if (userService.getUserByEmail(email).isEmpty()) {
            User admin = new User();
            admin.setFirstName(firstName);
            admin.setLastName(lastName);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setEmail(email);
            admin.setAge(age);
            admin.setRoles(Set.of(roleService.getRoleByName("ROLE_ADMIN").orElseThrow(RoleNotFoundException::new),
                    roleService.getRoleByName("ROLE_USER").orElseThrow(RoleNotFoundException::new)));
            admin.setAdmin(true);
            userService.addUser(admin);
        }
    }
}
