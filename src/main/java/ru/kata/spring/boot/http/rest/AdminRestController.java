package ru.kata.spring.boot.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.boot.dtos.UserRequestDto;
import ru.kata.spring.boot.dtos.UserResponseDto;
import ru.kata.spring.boot.exceptions.UserNotFoundException;
import ru.kata.spring.boot.mappers.UserMapper;
import ru.kata.spring.boot.models.User;
import ru.kata.spring.boot.services.UserService;
import ru.kata.spring.boot.utils.UserValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminRestController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<UserResponseDto> userResponseDTOs = users.stream()
                .map(userMapper::toUserResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponseDTOs);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUser(@PathVariable("id") Long id) {
        return userMapper.toUserResponseDto(userService.getUserById(id)
                .orElseThrow(UserNotFoundException::new));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserRequestDto createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        User userForCreate = userService.addUser(userMapper.toUserEntity(userRequestDto));
        return userMapper.toRequestDto(userForCreate);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserResponseDto userResponseDto,
                                      BindingResult result) {
        User userForUpdate = userService.getUserById(id).orElseThrow(UserNotFoundException::new);
        if (!id.equals(userResponseDto.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        userValidator.validate(userResponseDto, result);
        if (result.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotFoundException(errorMsg.toString());
        }
        userService.updateUser(userMapper.toUserEntity(userResponseDto));
        return userMapper.toUserResponseDto(userForUpdate);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
