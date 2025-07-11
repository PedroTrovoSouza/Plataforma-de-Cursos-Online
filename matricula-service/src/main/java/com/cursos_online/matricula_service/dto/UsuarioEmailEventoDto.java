package com.cursos_online.matricula_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEmailEventoDto {
    private String nome;
    private String email;
}
