package com.cursos.controller;
import com.cursos.dto.avaliacao.AvaliacaoListagemDto;
import com.cursos.dto.avaliacao.AvaliacaoRequestDto;
import com.cursos.dto.avaliacao.AvaliacaoResponseDto;
import com.cursos.entity.Avaliacao;
import com.cursos.mapper.AvaliacaoMapper;
import com.cursos.service.AvaliacaoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @GetMapping
    public ResponseEntity<List<AvaliacaoResponseDto>> listarAvaliacoes(){
        List<Avaliacao> avaliacoes = avaliacaoService.listarTodasAvaliacao();
        List<AvaliacaoResponseDto> response = avaliacoes.stream().map(AvaliacaoMapper::toResponseDto).toList();
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/avaliacoes-usuario/{id}")
    public ResponseEntity<List<AvaliacaoResponseDto>> listarAvaliacoesDoUsuario(@PathVariable("id") Long idUsuario){
        List<Avaliacao> avaliacoes = avaliacaoService.listarAvaliacaoDoUsuario(idUsuario);
        List<AvaliacaoResponseDto> response = avaliacoes.stream().map(AvaliacaoMapper::toResponseDto).toList();
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/avaliacoes-curso/{id}")
    public ResponseEntity<List<AvaliacaoResponseDto>> listarAvaliacoesDoCurso(@PathVariable("id") Long idCurso){
        List<Avaliacao> avaliacoes = avaliacaoService.listarAvaliacaoDoCurso(idCurso);
        List<AvaliacaoResponseDto> response = avaliacoes.stream().map(AvaliacaoMapper::toResponseDto).toList();
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AvaliacaoResponseDto> avaliar(@RequestBody AvaliacaoRequestDto avaliacaoCadastro){
        Avaliacao avaliacao = AvaliacaoMapper.toEntity(avaliacaoCadastro);
        Avaliacao avaliacaoCadastrada = avaliacaoService.avaliarCurso(avaliacao, avaliacaoCadastro.idCurso());
        AvaliacaoResponseDto response = AvaliacaoMapper.toResponseDto(avaliacaoCadastrada);
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/nota/{id}")
    public ResponseEntity<AvaliacaoResponseDto> atualizarNotaDaAvaliacao(@PathVariable("id") Long idAvaliacao, @RequestBody Double nota){
        Avaliacao avaliacao = avaliacaoService.alterarNotadaAvaliacao(idAvaliacao, nota);
        return ResponseEntity.ok(AvaliacaoMapper.toResponseDto(avaliacao));
    }

    @PatchMapping("/comentario/{id}")
    public ResponseEntity<AvaliacaoResponseDto> atualizarNotaDaAvaliacao(@PathVariable("id") Long idAvaliacao, @RequestBody String comentario){
        Avaliacao avaliacao = avaliacaoService.alterarComentariodaAvaliacao(idAvaliacao, comentario);
        return ResponseEntity.ok(AvaliacaoMapper.toResponseDto(avaliacao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarAvaliacao(@PathVariable("id") Long idAvaliacao){
        avaliacaoService.deletarAvaliacao(idAvaliacao);
        return ResponseEntity.ok().build();
    }
}
