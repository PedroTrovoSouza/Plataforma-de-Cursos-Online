package com.cursos_online.service;

import com.cursos_online.entity.Avaliacao;
import com.cursos_online.exception.AvaliacaoNaoEncontradaException;
import com.cursos_online.repository.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private AvaliacaoRepository avaliacaoRepository;

    public List<Avaliacao> listarTodasAvaliacao(){
        return avaliacaoRepository.findAll();
    }

    public List<Avaliacao> listarAvaliacaoDoUsuario(Long idUsuario){
        return avaliacaoRepository.findAllByIdUsuario(idUsuario);
    }

    public List<Avaliacao> listarAvaliacaoDoCurso(Long idCurso){
        return avaliacaoRepository.findAllByCursos_Id(idCurso);
    }

    public Avaliacao avaliarCurso(Avaliacao avaliacao){
        return avaliacaoRepository.save(avaliacao);
    }

    public Avaliacao buscarAvaliacaoPorId(Long id){
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new AvaliacaoNaoEncontradaException("Avaliação com ID não encontrada."));
    }

    public Avaliacao alterarNotadaAvaliacao(Long id, Double novaNota){
        Avaliacao avaliacao = buscarAvaliacaoPorId(id);
        avaliacao.setNota(novaNota);
        return avaliacao;
    }

    public Avaliacao alterarComentariodaAvaliacao(Long id, String novoComentario){
        Avaliacao avaliacao = buscarAvaliacaoPorId(id);
        avaliacao.setComentario(novoComentario);
        return avaliacao;
    }

    public void deletarAvaliacao(Long id){
        Avaliacao avaliacao = buscarAvaliacaoPorId(id);
        avaliacaoRepository.delete(avaliacao);
    }

}
