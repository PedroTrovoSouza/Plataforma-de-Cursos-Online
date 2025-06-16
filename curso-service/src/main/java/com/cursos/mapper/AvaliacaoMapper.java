package com.cursos.mapper;

import com.cursos.dto.avaliacao.AvaliacaoRequestDto;
import com.cursos.dto.avaliacao.AvaliacaoResponseDto;
import com.cursos.entity.Avaliacao;

public class AvaliacaoMapper {

    public static AvaliacaoResponseDto toResponse (Avaliacao avaliacao, String nomeUsuario, String nomeCurso) {
        return new AvaliacaoResponseDto(avaliacao.getId(), avaliacao.getNota(), avaliacao.getComentario(),
                nomeUsuario, nomeCurso);
    }

    public static Avaliacao toEntity(AvaliacaoRequestDto avaliacaoCadastro) {
        return new Avaliacao(avaliacaoCadastro.nota(), avaliacaoCadastro.comentario(), avaliacaoCadastro.idUsuario());
    }
}
