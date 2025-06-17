package com.cursos.mapper;

import com.cursos.dto.avaliacao.AvaliacaoRequestDto;
import com.cursos.dto.avaliacao.AvaliacaoCadastroDto;
import com.cursos.entity.Avaliacao;

public class AvaliacaoMapper {

    public static AvaliacaoCadastroDto toResponse (Avaliacao avaliacao) {
        return new AvaliacaoCadastroDto(avaliacao.getId(), avaliacao.getComentario(),
                avaliacao.getNomeUsuario(), avaliacao.getCurso().getTitulo());
    }

    public static Avaliacao toEntity(AvaliacaoRequestDto avaliacaoCadastro) {
        return new Avaliacao(avaliacaoCadastro.nota(), avaliacaoCadastro.comentario(), avaliacaoCadastro.idUsuario());
    }
}
