package com.cursos_online.usuario_service.dto;

import lombok.Data;

@Data
public class UsuarioResponseDto {
    private String nome;
    private String email;
    private String tipo;

    public UsuarioResponseDto() {
    }

    public UsuarioResponseDto(String nome, String email, String tipo) {
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
    }
}
