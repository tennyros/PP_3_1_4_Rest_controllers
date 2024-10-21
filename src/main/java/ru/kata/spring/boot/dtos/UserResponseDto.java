package ru.kata.spring.boot.dtos;

import lombok.*;
import ru.kata.spring.boot.models.Role;

import javax.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDto {

    private Long id;

    @NotBlank(message = "Username must be defined!")
    @Size(min = 3, max = 18, message = "Username must be from 3 to 18 symbols long!")
    private String firstName;

    @NotBlank(message = "Username must be defined!")
    @Size(min = 3, max = 18, message = "Username must be from 3 to 18 symbols long!")
    private String lastName;

    @NotBlank(message = "Email must be defined!")
    @Email(message = "Email must be valid!")
    private String email;

    @NotNull(message = "Age must be defined!")
    @Min(value = 14, message = "Age must not be less than 14!")
    @Max(value = 125, message = "Age must not be more than 125!")
    private Integer age;

    @NotEmpty(message = "Role must be selected!")
    private Set<Role> roles;

    private boolean admin;
}
