//package ru.kata.spring.boot.http.handlers;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//import ru.kata.spring.boot.exceptions.*;
//
//import java.time.LocalDateTime;
//
//@RestControllerAdvice(basePackages = "ru.kata.spring.boot.http.rest")
//public class RestControllerExceptionHandlerNot extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler({UserNotFoundException.class, RoleNotFoundException.class})
//    protected ResponseEntity<UserErrorResponse> handleNotFoundException(RuntimeException e) {
//        UserErrorResponse userErrorResponse = new UserErrorResponse(
//                e.getMessage(),
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler({UserNotCreatedException.class, UserValidationException.class})
//    protected ResponseEntity<UserErrorResponse> handleBadRequestException(RuntimeException e) {
//        UserErrorResponse userErrorResponse = new UserErrorResponse(
//                e.getMessage(),
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(UserIdMismatchException.class)
//    protected ResponseEntity<UserErrorResponse> handleForbiddenException(UserIdMismatchException e) {
//        UserErrorResponse userErrorResponse = new UserErrorResponse(
//                e.getMessage(),
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(userErrorResponse, HttpStatus.FORBIDDEN);
//    }
//}
