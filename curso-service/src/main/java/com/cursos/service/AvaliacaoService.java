package com.cursos.service;
import com.cursos.dto.avaliacao.AvaliacaoCadastroDto;
import com.cursos.dto.usuario.UsuarioResponseDto;
import com.cursos.entity.Avaliacao;
import com.cursos.entity.Curso;
import com.cursos.exception.AvaliacaoNaoEncontradaException;
import com.cursos.mapper.AvaliacaoMapper;
import com.cursos.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;

    private final CursoService cursoService;

    private final WebClient webUsuario;

    @Autowired
    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository, CursoService cursoService, WebClient.Builder webClientBuilder) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.cursoService = cursoService;
        this.webUsuario = webClientBuilder.baseUrl("http://localhost:9091").build();
    }

    public List<Avaliacao> listarTodasAvaliacao(){
        return avaliacaoRepository.findAll();
    }

    public List<AvaliacaoCadastroDto> listarAvaliacaoDoUsuario(Long idUsuario){
        String urlUsuarios = "/usuario/" + idUsuario;
        UsuarioResponseDto user = webUsuario.get()
                .uri(urlUsuarios)
                .retrieve()
                .bodyToMono(UsuarioResponseDto.class)
                .block();
        List<Avaliacao> avaliacoes = avaliacaoRepository.findAllByIdUsuario(idUsuario);

        return avaliacoes.stream().map(avaliacao ->
                AvaliacaoMapper.toResponse(avaliacao, avaliacao.getCurso().getTitulo())).toList();
    }

    public List<Avaliacao> listarAvaliacaoDoCurso(Long idCurso){
        return avaliacaoRepository.findAllByCursoId(idCurso);
    }

    public AvaliacaoCadastroDto avaliarCurso(Avaliacao avaliacao, Long idCurso){
        String urlUsuarios = "/usuario/" + avaliacao.getIdUsuario();
        UsuarioResponseDto user = webUsuario.get()
                .uri(urlUsuarios)
                .retrieve()
                .bodyToMono(UsuarioResponseDto.class)
                .block();
        avaliacao.setNomeUsuario(user.getNome());
        Curso curso = cursoService.buscarCursoPorId(idCurso);
        avaliacao.setCurso(curso);

        Avaliacao avaliacaoCadastrada = avaliacaoRepository.save(avaliacao);
        return AvaliacaoMapper.toResponse(avaliacaoCadastrada, curso.getTitulo());
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
