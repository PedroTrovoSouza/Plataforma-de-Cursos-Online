package com.cursos_online.mapper;

import com.cursos_online.dto.curso.CursoRequestDto;
import com.cursos_online.dto.curso.CursoResponseDto;
import com.cursos_online.entity.Curso;

public class CursoMapper {

    public static CursoResponseDto toResponseDto(Curso entity){
        return new CursoResponseDto(entity.getTitulo(), entity.getDescricao(), entity.getCategoria(), entity.getPreco(), entity.getNota());
    }

    public static Curso toEntity(CursoRequestDto cursoParaCadastrar) {
        return new Curso(cursoParaCadastrar.titulo(), cursoParaCadastrar.descricao(), cursoParaCadastrar.categoria(), cursoParaCadastrar.preco());
    }
}
