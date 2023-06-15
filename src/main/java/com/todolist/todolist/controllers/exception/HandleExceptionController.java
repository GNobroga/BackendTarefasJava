package com.todolist.todolist.controllers.exception;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.todolist.todolist.exceptions.ObjectNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class HandleExceptionController {
    
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Error objetoNaoEncontradoException(ObjectNotFoundException e, HttpServletRequest req) {
        return new Error(
            LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
            HttpStatus.BAD_REQUEST.value(),
            "Objeto não encontrado",
            e.getMessage(),
            req.getRequestURI()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Error illegalArgumentException(IllegalArgumentException e, HttpServletRequest req) {
          return new Error(
            LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
            HttpStatus.BAD_REQUEST.value(),
            "Operacao Ilegal",
            e.getMessage(),
            req.getRequestURI()
        );
    }

}
