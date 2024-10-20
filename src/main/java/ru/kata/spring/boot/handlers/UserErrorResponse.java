package ru.kata.spring.boot.handlers;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserErrorResponse {

    private String error;
    private LocalDateTime timestamp;
}
