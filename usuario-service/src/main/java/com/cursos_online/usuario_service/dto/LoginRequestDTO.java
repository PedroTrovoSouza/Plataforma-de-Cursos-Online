package com.cursos_online.usuario_service.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    String email;
    String senha;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }
}
