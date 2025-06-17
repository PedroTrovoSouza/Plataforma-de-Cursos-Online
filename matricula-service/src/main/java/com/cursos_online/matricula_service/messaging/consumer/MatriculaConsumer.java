package com.cursos_online.matricula_service.messaging.consumer;

import com.cursos_online.matricula_service.messaging.config.RabbitMQConfig;
import com.cursos_online.matricula_service.service.EmailService;
import com.cursos_online.usuario_service.dto.UsuarioEmailEventoDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatriculaConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_MATRICULA_REALIZADA)
    public void consumirEventoMatriculaRealizada(UsuarioEmailEventoDto eventoDto) {
        emailService.enviarEmail(
                eventoDto.getEmail(),
                "Matrícula realizada",
                "Olá " + eventoDto.getNome() + ", matrícula realizada com sucesso"
        );
    }
}
