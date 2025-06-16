package com.cursos.controller;
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
    public ResponseEntity<List<Avaliacao>> listarAvaliacoes(){
        List<Avaliacao> response = avaliacaoService.listarTodasAvaliacao();
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("/avaliacoes-usuario/{id}")
    public ResponseEntity<List<AvaliacaoResponseDto>> listarAvaliacoesDoUsuario(@PathVariable Long idUsuario){
        List<AvaliacaoResponseDto> response = avaliacaoService.listarAvaliacaoDoUsuario(idUsuario);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @PostMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDto> avaliar(@RequestBody AvaliacaoRequestDto avaliacaoCadastro){
        Avaliacao avaliacao = AvaliacaoMapper.toEntity(avaliacaoCadastro);
        AvaliacaoResponseDto response = avaliacaoService.avaliarCurso(avaliacao, avaliacaoCadastro.idCurso());
        return ResponseEntity.status(201).body(response);
    }
}
