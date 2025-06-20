package com.cursos.dto.curso;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursoResponseDto {
    String titulo;
    String descricao;
    String categoria;
    Double preco;
    Double nota;
}

