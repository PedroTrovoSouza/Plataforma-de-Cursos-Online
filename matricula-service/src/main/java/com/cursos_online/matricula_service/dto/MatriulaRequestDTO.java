package com.cursos_online.matricula_service.dto;

import lombok.Data;

@Data
public class MatriulaRequestDTO {
    String nomeCurso;
    private Long idCurso;
    private Long idUsuario;
}
