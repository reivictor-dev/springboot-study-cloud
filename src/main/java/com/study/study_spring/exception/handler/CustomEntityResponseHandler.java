package com.study.study_spring.exception.handler;

import java.util.Date;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.study.study_spring.exception.ExceptionResponse;
import com.study.study_spring.exception.FileNotFoundException;
import com.study.study_spring.exception.FileStorageException;
import com.study.study_spring.exception.InvalidJWTAuthenticationException;
import com.study.study_spring.exception.RequiredObjectIsNullException;
import com.study.study_spring.exception.ResourceNotFoundException;

@ControllerAdvice
@RestController
public class CustomEntityResponseHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
            new Date(),
            ex.getMessage(),
            request.getDescription(false)
            );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
            new Date(),
            ex.getMessage(),
            request.getDescription(false)
            );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequiredObjectIsNullException.class)
    public final ResponseEntity<ExceptionResponse> handleRequiredObjectExceptions(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
            new Date(),
            ex.getMessage(),
            request.getDescription(false)
            );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleFileNotFoundException(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
            new Date(),
            ex.getMessage(),
            request.getDescription(false)
            );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileStorageException.class)
    public final ResponseEntity<ExceptionResponse> handleFileStorageException(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
            new Date(),
            ex.getMessage(),
            request.getDescription(false)
            );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestExceptions(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
            new Date(),
            ex.getMessage(),
            request.getDescription(false)
            );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJWTAuthenticationException.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidJWTAuthenticationExceptions(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
            new Date(),
            ex.getMessage(),
            request.getDescription(false)
            );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
