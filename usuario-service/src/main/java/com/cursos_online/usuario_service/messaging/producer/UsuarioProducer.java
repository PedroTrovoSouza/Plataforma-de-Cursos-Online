package com.cursos_online.usuario_service.messaging.producer;

import com.cursos_online.usuario_service.dto.UsuarioEmailEventoDto;
import com.cursos_online.usuario_service.messaging.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void enviarEventoUsuarioCriado(UsuarioEmailEventoDto eventoDto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, eventoDto);
        System.out.println("[Producer] Evento de usuário cadastrado enviado: " + eventoDto);
    }
}