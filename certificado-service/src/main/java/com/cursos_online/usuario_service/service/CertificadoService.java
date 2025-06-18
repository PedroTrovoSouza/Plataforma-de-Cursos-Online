package com.cursos_online.usuario_service.service;

import com.cursos_online.usuario_service.dto.CertificadoDto;
import com.cursos_online.usuario_service.entity.Certificado;
import com.cursos_online.usuario_service.mapper.CertificadoMapper;
import com.cursos_online.usuario_service.messaging.producer.UsuarioProducer;
import com.cursos_online.usuario_service.repository.CertificadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificadoService {

    private CertificadoRepository repository;

    @Autowired
    private UsuarioProducer usuarioProducer;

    public CertificadoService(CertificadoRepository repository) {
        this.repository = repository;
    }

    public CertificadoDto cadastrarCertificado(CertificadoDto dto) {
        if (dto == null || dto.getMatriculaId() == null) {
            return null;
        }

        Certificado certificado = CertificadoMapper.toEntity(dto);
        repository.save(certificado);

        // Envia o evento para a fila
        usuarioProducer.enviarEventoUsuarioCriado(dto);

        return CertificadoMapper.toDto(certificado);
    }

}
