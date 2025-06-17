package com.cursos_online.usuario_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CertificadoDto {

    private LocalDate dataEmissao;
    private Long matriculaId;
}
