package ru.kata.spring.boot.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot.dtos.UserRequestDto;
import ru.kata.spring.boot.models.User;
import ru.kata.spring.boot.services.UserService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRequestDto userRequestDto = (UserRequestDto) target;

        Optional<User> userByEmail = userService.getUserByEmail(userRequestDto.getEmail());
        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(userRequestDto.getId())) {
            errors.rejectValue("email", "", "User with such email is already exists!");
        }

        if (!userRequestDto.getPassword().equals(userRequestDto.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "error.userDto",
                    "Passwords do not match!");
        }
    }
}
