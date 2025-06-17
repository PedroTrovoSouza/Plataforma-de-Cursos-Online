package com.cursos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class NotaDeAvaliacaoInvalida extends RuntimeException {
    public NotaDeAvaliacaoInvalida(String message) {
        super(message);
    }
}
