package com.cursos_online.usuario_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UsuarioNaoEncontradoException extends RuntimeException{
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }
}
