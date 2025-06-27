package com.cursos_online.conteudo_service.controller;

import com.cursos_online.conteudo_service.dto.AtualizarConteudoDTO;
import com.cursos_online.conteudo_service.dto.ListarConteudoDTO;
import com.cursos_online.conteudo_service.entity.Conteudo;
import com.cursos_online.conteudo_service.service.ConteudoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/conteudos")
public class ConteudoController {

    private final ConteudoService conteudoService;

    @Autowired
    private S3Client s3Client;

    public ConteudoController(ConteudoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    @PostMapping
    public ResponseEntity salvar(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket("curso-bucket")
                            .key(file.getOriginalFilename())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );
            return ResponseEntity.status(201).body("Arquivo salvo com sucesso");
        }catch (RuntimeException e){
            return ResponseEntity.status(400).body("Deu merda pai");
        }
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

    @PutMapping("/{id}")
    public ResponseEntity<ListarConteudoDTO> atualizar(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody AtualizarConteudoDTO dto) {
        Conteudo conteudoAtualizado = conteudoService.atualizar(id, dto);
        return ResponseEntity.ok(new ListarConteudoDTO(conteudoAtualizado.getId(), conteudoAtualizado.getTitulo(), conteudoAtualizado.getUrl_video(), conteudoAtualizado.getCursoId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        conteudoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}