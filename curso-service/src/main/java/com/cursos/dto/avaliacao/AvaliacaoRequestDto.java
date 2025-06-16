package com.cursos.dto.avaliacao;

public record AvaliacaoRequestDto(Double nota, String comentario, Long idUsuario, Long idCurso) {
}
