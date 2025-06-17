package com.cursos.controller;
import com.cursos.dto.avaliacao.AvaliacaoRequestDto;
import com.cursos.dto.avaliacao.AvaliacaoCadastroDto;
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
    public ResponseEntity<List<Avaliacao>> listarAvaliacoes(){
        List<Avaliacao> response = avaliacaoService.listarTodasAvaliacao();
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/avaliacoes-usuario/{id}")
    public ResponseEntity<List<AvaliacaoCadastroDto>> listarAvaliacoesDoUsuario(@PathVariable Long idUsuario){
        List<AvaliacaoCadastroDto> response = avaliacaoService.listarAvaliacaoDoUsuario(idUsuario);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/avaliacoes-curso/{id}")
    public ResponseEntity<List<AvaliacaoCadastroDto>> listarAvaliacoesDoCurso(@PathVariable Long idCurso){
        List<Avaliacao> avaliacoes = avaliacaoService.listarAvaliacaoDoCurso(idCurso);
        List<AvaliacaoCadastroDto> response = avaliacoes.stream().map(AvaliacaoMapper::toResponse).toList();
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @PostMapping("/{id}")
    public ResponseEntity<AvaliacaoCadastroDto> avaliar(@RequestBody AvaliacaoRequestDto avaliacaoCadastro){
        Avaliacao avaliacao = AvaliacaoMapper.toEntity(avaliacaoCadastro);
        Avaliacao avaliacaoCadastrada = avaliacaoService.avaliarCurso(avaliacao, avaliacaoCadastro.idCurso());
        AvaliacaoCadastroDto response = AvaliacaoMapper.toResponse(avaliacaoCadastrada);
        return ResponseEntity.status(201).body(response);
    }
}
