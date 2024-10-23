package ru.kata.spring.boot.http.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot.dtos.OnCreate;
import ru.kata.spring.boot.dtos.OnUpdate;
import ru.kata.spring.boot.dtos.UserRequestDto;
import ru.kata.spring.boot.dtos.UserResponseDto;
import ru.kata.spring.boot.exceptions.UserIdMismatchException;
import ru.kata.spring.boot.exceptions.UserNotCreatedException;
import ru.kata.spring.boot.exceptions.UserNotFoundException;
import ru.kata.spring.boot.exceptions.UserValidationException;
import ru.kata.spring.boot.mappers.UserMapper;
import ru.kata.spring.boot.models.User;
import ru.kata.spring.boot.services.RegistrationService;
import ru.kata.spring.boot.services.UserService;
import ru.kata.spring.boot.utils.UserValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminRestController {

    private static final Logger log = LoggerFactory.getLogger(AdminRestController.class);
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<UserResponseDto> userResponseDTOs = users.stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponseDTOs);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUser(@PathVariable("id") Long id) {
        return userMapper.toResponseDto(userService.getUserById(id)
                .orElseThrow(UserNotFoundException::new));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @Validated(OnCreate.class) @RequestBody
                                         UserRequestDto userRequestDto, BindingResult result) {

        userValidator.validate(userRequestDto, result);
        if (result.hasErrors()) {
            throw new UserNotCreatedException(validationErrorMessageInit(result).toString());
        }
        User userEntity = userMapper.requestToEntity(userRequestDto);
        userService.mapRolesForNewUser(userRequestDto, userEntity);
        registrationService.register(userEntity);
        return userMapper.toResponseDto(userEntity);
    }

    @PutMapping( "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUser(@Validated(OnUpdate.class) @PathVariable("id") Long id,
                                      @Valid @RequestBody UserRequestDto userRequestDto, BindingResult result) {

        User userForUpdate = userService.getUserById(id).orElseThrow(UserNotFoundException::new);
        userValidator.validate(userRequestDto, result);
        if (result.hasErrors()) {
            throw new UserValidationException(validationErrorMessageInit(result).toString());
        }
        if (!id.equals(userRequestDto.getId())) {
            throw new UserIdMismatchException();
        }
        userService.mapAndUpdateRoles(userRequestDto, userForUpdate);
        userMapper.updateUserFromDto(userRequestDto, userForUpdate);
        userService.updateUser(userForUpdate);
        return userMapper.toResponseDto(userForUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException();
        }
    }

    private StringBuilder validationErrorMessageInit(BindingResult result) {
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> errors = result.getFieldErrors();
        for (FieldError error : errors) {
            errorMsg.append(error.getField())
                    .append(" - ").append(error.getDefaultMessage())
                    .append(";");
        }
        return errorMsg;
    }
}
