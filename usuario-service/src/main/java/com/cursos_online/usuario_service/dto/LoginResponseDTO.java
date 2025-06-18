package com.cursos_online.usuario_service.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    String token;

    public LoginResponseDTO(String token) {
        this.token = token;
    }
}
