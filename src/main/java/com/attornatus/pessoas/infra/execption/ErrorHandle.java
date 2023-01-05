package com.attornatus.pessoas.infra.execption;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandle {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleErro404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleErro400(MethodArgumentNotValidException ex){

        var erros = ex.getFieldErrors();

        return ResponseEntity.badRequest().body(erros.stream().map(DataErros::new).toList());
    }

    private record DataErros(String field, String message){
        public DataErros(FieldError erro){
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

}