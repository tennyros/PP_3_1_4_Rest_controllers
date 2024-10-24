package ru.kata.spring.boot.http.handlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.kata.spring.boot.exceptions.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "ru.kata.spring.boot.http.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotFoundException.class, RoleNotFoundException.class})
    protected ResponseEntity<UserErrorResponse> handleNotFoundException(BaseException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(e.getMessage(), e.getTimestamp());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserValidationException.class})
    protected ResponseEntity<UserErrorResponse> handleValidationException(UserValidationException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(e.getFieldErrors(), e.getTimestamp());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotCreatedException.class})
    protected ResponseEntity<UserErrorResponse> handleNotCreatedException(UserNotCreatedException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(e.getMessage(), e.getTimestamp());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIdMismatchException.class)
    protected ResponseEntity<UserErrorResponse> handleForbiddenException(UserIdMismatchException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(e.getMessage(), e.getTimestamp());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.FORBIDDEN);
    }
}
