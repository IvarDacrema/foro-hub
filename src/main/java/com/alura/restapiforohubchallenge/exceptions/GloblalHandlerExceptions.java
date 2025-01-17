package com.alura.restapiforohubchallenge.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.alura.restapiforohubchallenge.exceptions.exceptions.ValidationException;

@RestControllerAdvice
public class GloblalHandlerExceptions {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handlingValidationsExceptions(ValidationException e) {
        return  ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}
