package com.cursos_online.usuario_service.mapper;

import com.cursos_online.usuario_service.dto.CertificadoDto;
import com.cursos_online.usuario_service.entity.Certificado;
import org.springframework.stereotype.Component;

@Component
public class CertificadoMapper {

    public static Certificado toEntity(CertificadoDto dto) {
        if (dto == null) {
            return null;
        }

        Certificado certificado = new Certificado();
        certificado.setDataEmissao(dto.getDataEmissao());
        certificado.setMatriculaId(dto.getMatriculaId());
        return certificado;
    }

    public static CertificadoDto toDto(Certificado entity) {
        if (entity == null) {
            return null;
        }

        CertificadoDto dto = new CertificadoDto();
        dto.setDataEmissao(entity.getDataEmissao());
        dto.setMatriculaId(entity.getMatriculaId());
        return dto;
    }
}

