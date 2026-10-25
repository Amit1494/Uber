package com.rideshare.rideservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice

@Slf4j
public class GlobalExceptionHandler {
    public ResponseEntity<Map<String,String>> handleValidation(
            MethodArgumentNotValidException ex
    ){
        Map<String,String> errors= new HashMap<>();
    }

}
