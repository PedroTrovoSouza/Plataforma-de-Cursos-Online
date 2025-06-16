package com.cursos_online.usuario_service.messaging.consumer;

import com.cursos_online.usuario_service.dto.UsuarioEmailEventoDto;
import com.cursos_online.usuario_service.messaging.config.RabbitMQConfig;
import com.cursos_online.usuario_service.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumirEventoUsuarioCriado(UsuarioEmailEventoDto eventoDto) {
        emailService.enviarEmail(
                eventoDto.getEmail(),
                "Bem-vindo à Plataforma - Começou o Curso",
                "Olá " + eventoDto.getNome() + ", seja bem-vindo!"
        );
    }
}