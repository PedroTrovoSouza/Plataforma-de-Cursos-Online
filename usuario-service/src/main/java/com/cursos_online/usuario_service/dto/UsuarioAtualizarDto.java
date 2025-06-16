package com.cursos_online.usuario_service.dto;

import lombok.Data;

@Data
public class UsuarioAtualizarDto {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String tipo;

    public UsuarioAtualizarDto() {
    }

    public UsuarioAtualizarDto(Long id, String nome, String email, String senha, String tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }
}
