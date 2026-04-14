package com.jwttemplate.api.auth.controller.advice;

import com.jwttemplate.api.auth.controller.exception.UserAlreadyRegisteredException;
import com.jwttemplate.api.auth.controller.exception.UserNotFoundException;
import com.jwttemplate.api.utils.common_response.CommonErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class LoginControllerAdvice {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CommonErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new CommonErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<CommonErrorResponse> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CommonErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CommonErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new CommonErrorResponse("Invalid email or password"));
    }
}
