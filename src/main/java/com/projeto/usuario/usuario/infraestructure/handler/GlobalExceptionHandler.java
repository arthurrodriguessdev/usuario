package com.projeto.usuario.usuario.infraestructure.handler;

import com.projeto.usuario.usuario.exception.ConflictException;
import com.projeto.usuario.usuario.exception.ResourceNotFound;
import com.projeto.usuario.usuario.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConflictException.class)
    private ResponseEntity<GlobalErrorMessage> ConflictExceptionHandler(ConflictException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new GlobalErrorMessage(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFound.class)
    private ResponseEntity<GlobalErrorMessage> ResourceNotFoundHandler(ResourceNotFound ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new GlobalErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    private ResponseEntity<GlobalErrorMessage> UnauthorizedExceptionHandler(UnauthorizedException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new GlobalErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage()));
    }
}