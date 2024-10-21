package ru.kata.spring.boot.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kata.spring.boot.dtos.UserRequestDto;
import ru.kata.spring.boot.dtos.UserResponseDto;
import ru.kata.spring.boot.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUserEntity(UserRequestDto userRequestDto);
    UserRequestDto toRequestDto(User user);
    @Mapping(target = "roles", ignore = true)
    void updateUserFromDto(UserRequestDto userRequestDto, @MappingTarget User user);

    @Mapping(target = "password", ignore = true)
    User toUserEntity(UserResponseDto userResponseDto);
    UserResponseDto toUserResponseDto(User user);
}