package com.cursos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class CursoConflitoException extends RuntimeException {
    public CursoConflitoException(String message) {
        super(message);
    }
}
