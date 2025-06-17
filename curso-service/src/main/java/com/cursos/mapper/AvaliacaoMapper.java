package com.cursos.mapper;

import com.cursos.dto.avaliacao.AvaliacaoRequestDto;
import com.cursos.dto.avaliacao.AvaliacaoResponseDto;
import com.cursos.entity.Avaliacao;

public class AvaliacaoMapper {

    public static AvaliacaoResponseDto toResponseDto(Avaliacao avaliacao) {
        return new AvaliacaoResponseDto(avaliacao.getId(), avaliacao.getNota(), avaliacao.getComentario(),
                avaliacao.getNomeUsuario(), avaliacao.getCurso().getTitulo());
    }

    public static Avaliacao toEntity(AvaliacaoRequestDto avaliacaoCadastro) {
        return new Avaliacao(avaliacaoCadastro.nota(), avaliacaoCadastro.comentario(), avaliacaoCadastro.idUsuario());
    }
}
