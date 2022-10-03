package com.assignment.home.assignment.controller;

import com.assignment.home.assignment.exceptions.NotFoundException;
import com.assignment.home.assignment.exceptions.PinAuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Map<String,String>> notFoundExceptionHandler(NotFoundException exception, WebRequest request)
    {
        Map<String,String> responseMap = new HashMap<>();
        responseMap.put("message", exception.getMessage());
        return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {PinAuthenticationException.class})
    public ResponseEntity<Map<String,String>> pinAuthenticationExceptionHandler(PinAuthenticationException exception, WebRequest request)
    {
        Map<String,String> responseMap = new HashMap<>();
        responseMap.put("message", exception.getMessage());
        return new ResponseEntity<>(responseMap, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {SQLSyntaxErrorException.class})
    public ResponseEntity<Map<String,String>> sqlSyntaxErrorHandler(SQLSyntaxErrorException exception, WebRequest request)
    {
        Map<String,String> responseMap = new HashMap<>();
        responseMap.put("message", "Database not found");
        return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Map<String,String>> runtimeExceptionHandler(RuntimeException exception, WebRequest request)
    {
        Map<String,String> responseMap = new HashMap<>();
        responseMap.put("message", exception.getMessage());
        return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
          HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        Map<String,String> responseMap = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach
                ((error)->
                {
                    responseMap.put(((FieldError) error).getField(), error.getDefaultMessage());
                });
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }
}
