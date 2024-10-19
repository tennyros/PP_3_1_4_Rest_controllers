package ru.kata.spring.boot.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot.dtos.UserDto;
import ru.kata.spring.boot.models.User;
import ru.kata.spring.boot.services.UserService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;

        Optional<User> userByEmail = userService.getUserByEmail(userDto.getEmail());
        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(userDto.getId())) {
            errors.rejectValue("email", "", "User with such email is already exists!");
        }

        if (!userDto.getPassword().equals(userDto.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "error.userDto",
                    "Passwords do not match!");
        }
    }
}
