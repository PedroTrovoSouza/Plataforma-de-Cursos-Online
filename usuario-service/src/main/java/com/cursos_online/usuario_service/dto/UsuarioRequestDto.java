package com.cursos_online.usuario_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDto {
    private String nome;
    private String email;
    private String senha;
    private String tipo;
}
