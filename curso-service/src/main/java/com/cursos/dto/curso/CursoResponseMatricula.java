package com.cursos.dto.curso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CursoResponseMatricula {
    private Long id;
    private String titulo;
    String descricao;
    String categoria;
    Double preco;
    Double nota;
}
