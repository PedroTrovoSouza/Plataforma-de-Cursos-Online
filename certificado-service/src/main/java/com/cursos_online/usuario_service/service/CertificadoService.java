package com.cursos_online.usuario_service.service;

import com.cursos_online.usuario_service.dto.CertificadoDto;
import com.cursos_online.usuario_service.entity.Certificado;
import com.cursos_online.usuario_service.mapper.CertificadoMapper;
import com.cursos_online.usuario_service.repository.CertificadoRepository;
import org.springframework.stereotype.Service;

@Service
public class CertificadoService {

    private CertificadoRepository repository;

    public CertificadoService(CertificadoRepository repository) {
        this.repository = repository;
    }

    public CertificadoDto cadastrarCertificado(CertificadoDto dto) {
        if (dto == null || dto.getMatriculaId() == null) {
            return null;
        }

        Certificado certificado = CertificadoMapper.toEntity(dto);
        repository.save(certificado);
        CertificadoDto responseDto = CertificadoMapper.toDto(certificado);
        return responseDto;
    }
}
