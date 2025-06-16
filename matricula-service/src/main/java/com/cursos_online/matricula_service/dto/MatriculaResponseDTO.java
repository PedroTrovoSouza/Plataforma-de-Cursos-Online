package com.cursos_online.matricula_service.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MatriculaResponseDTO {
    Date data_matricula;
    String status;
    String nomeCurso;
}
