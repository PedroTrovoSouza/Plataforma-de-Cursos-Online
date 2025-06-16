package com.cursos_online.conteudo_service.service;

import com.cursos_online.conteudo_service.dto.AtualizarConteudoDTO;
import com.cursos_online.conteudo_service.dto.CadastrarConteudoDTO;
import com.cursos_online.conteudo_service.dto.CursoDTO;
import com.cursos_online.conteudo_service.entity.Conteudo;
import com.cursos_online.conteudo_service.repository.ConteudoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ConteudoService {

    private final ConteudoRepository conteudoRepository;
    private final WebClient webClient;

    public ConteudoService(ConteudoRepository conteudoRepository) {
        this.conteudoRepository = conteudoRepository;
        this.webClient = WebClient.create("http://localhost:8081");
    }

    public List<Conteudo> listarTodos() {
        return conteudoRepository.findAll();
    }

    public Conteudo buscarPorId(Long id) {
        return conteudoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conteúdo não encontrado com ID: " + id));
    }

    public List<Conteudo> buscarPorCursoId(Long cursoId) {
        CursoDTO cursoDTO = webClient.get()
                .uri("/cursos/{id}", cursoId)
                .retrieve()
                .bodyToMono(CursoDTO.class)
                .block();

        if (cursoDTO == null) {
            throw new RuntimeException("Curso não encontrado");
        }
        return conteudoRepository.findByCursoId(cursoId);
    }

    public Conteudo salvar(CadastrarConteudoDTO dto) {
        Conteudo conteudo = new Conteudo();
        conteudo.setTitulo(dto.titulo());
        conteudo.setUrl_video(dto.url_video());
        return conteudoRepository.save(conteudo);
    }

    public Conteudo atualizar(Long id, AtualizarConteudoDTO dto) {
        Conteudo conteudo = buscarPorId(id);
        conteudo.setTitulo(dto.titulo());
        return conteudoRepository.save(conteudo);
    }

    public void excluir(Long id) {
        conteudoRepository.deleteById(id);
    }
}