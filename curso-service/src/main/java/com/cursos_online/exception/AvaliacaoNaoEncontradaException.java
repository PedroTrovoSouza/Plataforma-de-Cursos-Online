package com.cursos_online.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AvaliacaoNaoEncontradaException extends RuntimeException {
    public AvaliacaoNaoEncontradaException(String message) {
    }
}
