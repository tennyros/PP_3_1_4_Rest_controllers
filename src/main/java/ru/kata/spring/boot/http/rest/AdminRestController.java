package ru.kata.spring.boot.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.boot.dtos.UserDto;
import ru.kata.spring.boot.mappers.UserMapper;
import ru.kata.spring.boot.models.User;
import ru.kata.spring.boot.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<UserDto> userDTOs = users.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return userMapper.toUserDto(user);
    }


    @PostMapping(value = "/new_user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> registrationExecution(@Valid @RequestBody UserDto userDto) {
        userService.addUser(userMapper.toUserEntity(userDto));
        return ResponseEntity.ok(userMapper.toUserDto(userService.getUserById(userDto.getId())));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<UserDto> updateUserExecution(@Valid @RequestBody UserDto userDto) {
        userService.updateUser(userMapper.toUserEntity(userDto));
        return ResponseEntity.ok(userMapper.toUserDto(userService.getUserById(userDto.getId())));
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
