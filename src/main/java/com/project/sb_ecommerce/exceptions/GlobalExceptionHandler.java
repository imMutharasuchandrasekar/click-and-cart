package com.project.sb_ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/*
 * The GlobalExceptionHandler class is meant to handle and provide customisations for all the exceptions which may
 * occur. This ensures consistent and client friendly exception messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler( MethodArgumentNotValidException.class )
    public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler( MethodArgumentNotValidException e )
    {
        HashMap<String, String> responseMap = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach( err -> {
            String fieldName = ((FieldError)err).getField();
            String message = err.getDefaultMessage();

            responseMap.put( fieldName, message );
        });
        return new ResponseEntity<>( responseMap, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler( ResourceNotFoundException.class )
    public ResponseEntity<String> resourceNotFoundExceptionHandler( ResourceNotFoundException e )
    {
        String message = e.getMessage();
        return new ResponseEntity<>( message, HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler( APIException.class )
    public ResponseEntity<String> apiExceptionHandler( APIException e )
    {
        String message = e.getMessage();
        return  new ResponseEntity<>( message, HttpStatus.BAD_REQUEST );
    }
}
