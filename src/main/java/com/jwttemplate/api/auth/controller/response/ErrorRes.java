package com.jwttemplate.api.auth.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorRes {
    private HttpStatus status;
    private String message;
}
