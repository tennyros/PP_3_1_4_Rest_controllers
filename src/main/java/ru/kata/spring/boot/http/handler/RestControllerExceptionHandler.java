package ru.kata.spring.boot.http.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "ru.kata.spring.rest.http.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
}
