package com.cursos_online.usuario_service.dto;

import lombok.Data;

@Data
public class UsuarioDto {
    private Long id;
    private String nome;
    private String email;
}