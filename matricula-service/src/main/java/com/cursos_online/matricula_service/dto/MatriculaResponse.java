package com.cursos_online.matricula_service.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MatriculaResponse {
    Date data_matricula;
    String status;
}
