package com.cursos_online.usuario_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.ACCEPTED)
public class ProblemaDeConexaoComRabbitMq extends RuntimeException {
    public ProblemaDeConexaoComRabbitMq(String message) {
        super(message);
    }
}
