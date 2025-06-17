package com.cursos_online.matricula_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class MatriculaResponseDTO {
    LocalDate data_matricula;
    String status;
    String nomeCurso;
}
