package com.cursos.dto.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CursoRequestDto(@NotBlank @Size(min = 3) String titulo, @NotBlank String descricao, String categoria, @NotNull Double preco) {
}
