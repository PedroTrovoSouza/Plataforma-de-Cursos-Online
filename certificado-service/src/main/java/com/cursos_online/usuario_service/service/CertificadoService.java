package com.cursos_online.usuario_service.service;

import com.cursos_online.usuario_service.dto.CertificadoDto;
import com.cursos_online.usuario_service.dto.MatriculaResponseDTO;
import com.cursos_online.usuario_service.entity.Certificado;
import com.cursos_online.usuario_service.mapper.CertificadoMapper;
import com.cursos_online.usuario_service.messaging.producer.UsuarioProducer;
import com.cursos_online.usuario_service.repository.CertificadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CertificadoService {

    private final CertificadoRepository repository;
    private final UsuarioProducer usuarioProducer;
    private final WebClient webMatricula;

    public CertificadoService(
            CertificadoRepository repository,
            UsuarioProducer usuarioProducer,
            WebClient.Builder webClientBuilder
    ) {
        this.repository = repository;
        this.usuarioProducer = usuarioProducer;
        this.webMatricula = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public CertificadoDto cadastrarCertificado(CertificadoDto dto) {
        if (dto == null || dto.getMatriculaId() == null) {
            return null;
        }

        MatriculaResponseDTO matriculaDto = webMatricula.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/matricula/buscar-dados-matricula/{id}")
                        .build(dto.getMatriculaId()))
                .retrieve()
                .bodyToMono(MatriculaResponseDTO.class)
                .block();

        if (!matriculaDto.getStatus().equalsIgnoreCase("CONCLUIDA")) {
            throw new IllegalArgumentException("Matricula não concluída");
        }

        Certificado certificado = CertificadoMapper.toEntity(dto);
        repository.save(certificado);

        // Envia o evento para a fila
        usuarioProducer.enviarEventoUsuarioCriado(dto);

        return CertificadoMapper.toDto(certificado);
    }

}
