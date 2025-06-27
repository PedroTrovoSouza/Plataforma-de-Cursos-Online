package com.cursos_online.conteudo_service.controller;

import com.cursos_online.conteudo_service.dto.AtualizarConteudoDTO;
import com.cursos_online.conteudo_service.dto.CadastrarConteudoDTO;
import com.cursos_online.conteudo_service.dto.ListarConteudoDTO;
import com.cursos_online.conteudo_service.entity.Conteudo;
import com.cursos_online.conteudo_service.service.ConteudoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/conteudos")
public class ConteudoController {

    private final ConteudoService conteudoService;

    public ConteudoController(ConteudoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<ListarConteudoDTO>> buscarPorCurso(@PathVariable Long cursoId) {
        List<ListarConteudoDTO> conteudos = conteudoService.buscarPorCursoId(cursoId)
                .stream()
                .map(conteudo -> new ListarConteudoDTO(conteudo.getId(), conteudo.getTitulo(), conteudo.getUrlConteudo(), conteudo.getCursoId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(conteudos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListarConteudoDTO> buscarPorId(@PathVariable Long id) {
        Conteudo conteudo = conteudoService.buscarPorId(id);
        return ResponseEntity.ok(new ListarConteudoDTO(conteudo.getId(), conteudo.getTitulo(), conteudo.getUrlConteudo(), conteudo.getCursoId()));
    }

    @PostMapping("/upload")
    public ResponseEntity<ListarConteudoDTO> salvar(@RequestPart("file") MultipartFile file,
                                                    @RequestPart("dados") String dadosJson) throws IOException {

        CadastrarConteudoDTO dto = new ObjectMapper().readValue(dadosJson, CadastrarConteudoDTO.class);

        Conteudo novoConteudo = conteudoService.salvar(dto, file);
        return ResponseEntity.status(201).body(new ListarConteudoDTO(novoConteudo.getId(), novoConteudo.getTitulo(),
                novoConteudo.getUrlConteudo(), novoConteudo.getCursoId()));
    }

    @GetMapping("/download/{nome}")
    public ResponseEntity<byte[]> baixarConteudo(@PathVariable String nome){
        try {
            byte[] dados = conteudoService.baixarArquivo("meu-bucketon", nome);

            if (dados == null || dados.length == 0) {
                return ResponseEntity.notFound().build(); // ← se vazio, responde 404 e evita erro no Gateway
            }

            MediaType tipo = conteudoService.detectarTipoDeArquivo(nome);

            return ResponseEntity.ok()
                    .contentType(tipo)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nome + "\"")
                    .body(dados);
        } catch (S3Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListarConteudoDTO> atualizar(@PathVariable Long id, @RequestBody AtualizarConteudoDTO dto) {
        Conteudo conteudoAtualizado = conteudoService.atualizar(id, dto);
        return ResponseEntity.ok(new ListarConteudoDTO(conteudoAtualizado.getId(), conteudoAtualizado.getTitulo(), conteudoAtualizado.getUrlConteudo(), conteudoAtualizado.getCursoId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        conteudoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}