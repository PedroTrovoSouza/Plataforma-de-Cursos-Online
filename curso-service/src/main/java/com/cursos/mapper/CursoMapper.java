package com.cursos.mapper;

import com.cursos.dto.curso.CursoRequestDto;
import com.cursos.dto.curso.CursoResponseDto;
import com.cursos.entity.Curso;

public class CursoMapper {

    public static CursoResponseDto toResponseDto(Curso entity){
        return new CursoResponseDto(entity.getTitulo(), entity.getDescricao(), entity.getCategoria(), entity.getPreco(), entity.getNota());
    }

    public static Curso toEntity(CursoRequestDto cursoParaCadastrar) {
        return new Curso(cursoParaCadastrar.titulo(), cursoParaCadastrar.descricao(), cursoParaCadastrar.categoria(), cursoParaCadastrar.preco());
    }
}
