package com.cursos_online.usuario_service.messaging.consumer;

import com.cursos_online.usuario_service.dto.CertificadoDto;
import com.cursos_online.usuario_service.dto.MatriculaResponseDTO;
import com.cursos_online.usuario_service.dto.UsuarioDto;
import com.cursos_online.usuario_service.messaging.config.RabbitMQConfig;
import com.cursos_online.usuario_service.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UsuarioConsumer {

    @Autowired
    private EmailService emailService;

    private final WebClient webMatricula;
    private final WebClient webUsuario;

    public UsuarioConsumer(WebClient.Builder webClientBuilder) {
        this.webMatricula = webClientBuilder.baseUrl("http://localhost:8080").build();;
        this.webUsuario = webClientBuilder.baseUrl("http://localhost:8082").build();;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumirEventoUsuarioCriado(CertificadoDto eventoDto) {

        MatriculaResponseDTO matriculaDto = webMatricula.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/matricula/buscar-dados-matricula/{id}")
                        .build(eventoDto.getMatriculaId()))
                .retrieve()
                .bodyToMono(MatriculaResponseDTO.class)
                .block();

        UsuarioDto usuarioDto = webUsuario.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/usuario/{id}")
                        .build(matriculaDto.getIdUsuario()))
                .retrieve()
                .bodyToMono(UsuarioDto.class)
                .block();

        emailService.enviarEmail(
                usuarioDto.getEmail(),
                "Parabéns pelo seu certificado!",
                "Olá " + usuarioDto.getNome() + "receba seu certificado" +
                        "https://images.tcdn.com.br/img/img_prod/805126/certificado_02_829_1_671f71d5fde391b509840df5bcd9f1c4.jpg"
        );
    }
}