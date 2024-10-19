package ru.kata.spring.boot.mappers;

import org.mapstruct.Mapper;
import ru.kata.spring.boot.dtos.UserDto;
import ru.kata.spring.boot.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
    User toUserEntity(UserDto userDto);
}