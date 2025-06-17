package com.cursos_online.conteudo_service.controller;

import com.cursos_online.conteudo_service.dto.AtualizarConteudoDTO;
import com.cursos_online.conteudo_service.dto.CadastrarConteudoDTO;
import com.cursos_online.conteudo_service.dto.ListarConteudoDTO;
import com.cursos_online.conteudo_service.entity.Conteudo;
import com.cursos_online.conteudo_service.service.ConteudoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/conteudo")
public class ConteudoController {

    private final ConteudoService conteudoService;

    public ConteudoController(ConteudoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<ListarConteudoDTO>> buscarPorCurso(@PathVariable Long cursoId) {
        List<ListarConteudoDTO> conteudos = conteudoService.buscarPorCursoId(cursoId)
                .stream()
                .map(conteudo -> new ListarConteudoDTO(conteudo.getId(), conteudo.getTitulo(), conteudo.getUrl_video(), conteudo.getCursoId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(conteudos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListarConteudoDTO> buscarPorId(@PathVariable Long id) {
        Conteudo conteudo = conteudoService.buscarPorId(id);
        return ResponseEntity.ok(new ListarConteudoDTO(conteudo.getId(), conteudo.getTitulo(), conteudo.getUrl_video(), conteudo.getCursoId()));
    }

    @PostMapping
    public ResponseEntity<ListarConteudoDTO> salvar(@RequestBody CadastrarConteudoDTO dto) {
        Conteudo novoConteudo = conteudoService.salvar(dto);
        return ResponseEntity.status(201).body(new ListarConteudoDTO(novoConteudo.getId(), novoConteudo.getTitulo(), novoConteudo.getUrl_video(), novoConteudo.getCursoId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListarConteudoDTO> atualizar(@PathVariable Long id, @RequestBody AtualizarConteudoDTO dto) {
        Conteudo conteudoAtualizado = conteudoService.atualizar(id, dto);
        return ResponseEntity.ok(new ListarConteudoDTO(conteudoAtualizado.getId(), conteudoAtualizado.getTitulo(), conteudoAtualizado.getUrl_video(), conteudoAtualizado.getCursoId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        conteudoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}