package ru.kata.spring.boot.http.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot.dtos.UserRequestDto;
import ru.kata.spring.boot.exceptions.UserNotFoundException;
import ru.kata.spring.boot.models.User;
import ru.kata.spring.boot.services.RegistrationService;
import ru.kata.spring.boot.services.RoleService;
import ru.kata.spring.boot.services.UserService;
import ru.kata.spring.boot.mappers.UserMapper;
import ru.kata.spring.boot.utils.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;
    private final RegistrationService registrationService;
    private final RoleService roleService;
    private final UserValidator userValidator;
    private final UserMapper userMapper;

    private static final String ROLES = "roles";
    private static final String USERS = "users";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String ADMIN_PAGE = "/admin/admin";
    private static final String REDIRECT_ADMIN_PAGE = "redirect:/admin/admin";
    private static final String ERROR_MESSAGE = "errorMessage";

    @GetMapping(value = "/admin")
    public String adminFullInfo(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute(USERS, users);
        model.addAttribute("userRequestDto", new UserRequestDto());
        model.addAttribute(ROLES, roleService.getAllRoles());
        return ADMIN_PAGE;
    }

    @PostMapping(value = "/new_user")
    public String registrationExecution(@Valid @ModelAttribute("userRequestDto") UserRequestDto userRequestDto,
                                        BindingResult result, Model model) {
        userValidator.validate(userRequestDto, result);
        if (result.hasErrors()) {
            model.addAttribute(ROLES, roleService.getAllRoles());
            model.addAttribute(USERS, userService.getAllUsers());
            model.addAttribute("activeTab", "new-user");
            return ADMIN_PAGE;
        }
        User user = userMapper.requestToEntity(userRequestDto);
        registrationService.register(user);
        return REDIRECT_ADMIN_PAGE;
    }


    @PostMapping(value = "/update")
    public String updateUserExecution(@Valid @ModelAttribute("userRequestDto") UserRequestDto userRequestDto,
                                      BindingResult result, Model model) {
        userValidator.validate(userRequestDto, result);
        if (result.hasErrors()) {
            model.addAttribute(ROLES, roleService.getAllRoles());
            model.addAttribute(USERS, userService.getAllUsers());
            return ADMIN_PAGE;
        }
        User user = userMapper.requestToEntity(userRequestDto);
        userService.updateUser(user);
        return REDIRECT_ADMIN_PAGE;
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@ModelAttribute("userRequestDto") UserRequestDto userRequestDto, Principal principal, Model model) {
        User currentUser = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> {
                    log.error("Current user is not found for principal {}", principal.getName());
                    return new UserNotFoundException();
                });
        Long userId = userRequestDto.getId();
        User userToDelete = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
        if (userToDelete.getId() == 1) {
            model.addAttribute(ERROR_MESSAGE, "You cannot delete the super administrator!");
            model.addAttribute(USERS, userService.getAllUsers());
            model.addAttribute(ROLES, roleService.getAllRoles());
            return ADMIN_PAGE;
        }
        if (userToDelete.getRoles().stream().anyMatch(role ->
                role.getAuthority().equals(ADMIN_ROLE)) && currentUser.getId() != 1) {
            model.addAttribute(ERROR_MESSAGE, "Only super administrator can delete other administrators!");
            model.addAttribute(USERS, userService.getAllUsers());
            model.addAttribute(ROLES, roleService.getAllRoles());
            return ADMIN_PAGE;
        }
        try {
            userService.deleteUser(userId);
        } catch (UnsupportedOperationException e) {
            log.error("Error while deleting user with id: {}", userId, e);
            model.addAttribute(ERROR_MESSAGE, e.getMessage());
            return "error";
        }
        return REDIRECT_ADMIN_PAGE;
    }
}
