package com.cursos_online.matricula_service.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatriculaResponseDTO {
    private LocalDate dataMatricula;
    private String status;
    private String nomeCurso;
}
