package com.springsec.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Date;


@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<Error> handleUserAlreadyExistsException(RuntimeException exception, ServletWebRequest webRequest) {
        Error error = Error.builder()
                .timestamp(new Date())
                .status(409)
                .message(exception.getMessage())
                .path(webRequest.getRequest().getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = InvalidPasswordException.class)
    public ResponseEntity<Error> handleInvalidPasswordException(RuntimeException exception, ServletWebRequest webRequest) {
        Error error = Error.builder()
                .timestamp(new Date())
                .status(400)
                .message(exception.getMessage())
                .path(webRequest.getRequest().getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handle JWT Token Expired Exception
    @org.springframework.web.bind.annotation.ExceptionHandler(value = JwtTokenExpiredException.class)
    public ResponseEntity<Error> handleJwtTokenExpiredException(JwtTokenExpiredException exception, ServletWebRequest webRequest) {
        Error error = Error.builder()
                .timestamp(new Date())
                .status(401)
                .message(exception.getMessage())
                .path(webRequest.getRequest().getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    // Handle JWT Token Invalid Exception
    @org.springframework.web.bind.annotation.ExceptionHandler(value = JwtTokenInvalidException.class)
    public ResponseEntity<Error> handleJwtTokenInvalidException(JwtTokenInvalidException exception, ServletWebRequest webRequest) {
        Error error = Error.builder()
                .timestamp(new Date())
                .status(400)
                .message(exception.getMessage())
                .path(webRequest.getRequest().getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
