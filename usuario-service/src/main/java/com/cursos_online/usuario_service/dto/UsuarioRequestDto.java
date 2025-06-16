package com.cursos_online.usuario_service.dto;

import lombok.Data;

@Data
public class UsuarioRequestDto {
    private String nome;
    private String email;
    private String senha;
    private String tipo;

    public UsuarioRequestDto() {
    }

    public UsuarioRequestDto(String nome, String email, String senha, String tipo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }
}
