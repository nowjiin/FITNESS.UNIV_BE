package com.hecto.fitnessuniv.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hecto.fitnessuniv.dto.response.ResponseDTO;

@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class
    })
    public ResponseEntity<ResponseDTO> validationExceptionHandler(Exception exception) {
        return ResponseDTO.validationFail();
    }
}
