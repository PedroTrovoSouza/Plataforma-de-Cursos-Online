package com.cursos_online.conteudo_service.dto;

public record CadastrarConteudoDTO(
        String titulo,
        String url_video,
        Long cursoId
) {
}
