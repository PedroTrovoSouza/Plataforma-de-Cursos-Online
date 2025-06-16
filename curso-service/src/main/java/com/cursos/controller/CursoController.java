package com.cursos.controller;

import com.cursos.dto.curso.CursoRequestDto;
import com.cursos.dto.curso.CursoResponseDto;
import com.cursos.entity.Curso;
import com.cursos.mapper.CursoMapper;
import com.cursos.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoResponseDto>> listarCursosCadastrados(){
        List<Curso> cursos = cursoService.listarTodosCursos();
        List<CursoResponseDto> response = cursos.stream().map(CursoMapper::toResponseDto).toList();
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CursoResponseDto> cadastrarNovoCurso(@RequestBody CursoRequestDto cursoParaCadastrar){
        Curso curso = CursoMapper.toEntity(cursoParaCadastrar);
        return ResponseEntity.status(201).body(CursoMapper
                .toResponseDto(cursoService
                        .cadastrarNovoCurso(curso)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDto> buscarCursoPorId(@PathVariable Long id){
        Curso curso = cursoService.buscarCursoPorId(id);
        CursoResponseDto response = CursoMapper.toResponseDto(curso);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{nome}")
    public ResponseEntity<CursoResponseDto> buscarCursoPorNome(@PathVariable String nome){
        Curso curso = cursoService.buscarCursoPorNome(nome);
        CursoResponseDto response = CursoMapper.toResponseDto(curso);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/titulo/{id}")
    public ResponseEntity<CursoResponseDto> atualizarTitulo(@PathVariable Long id, @RequestBody String novoTitulo){
        Curso curso = cursoService.atualizarTituloDoCurso(id, novoTitulo);
        return ResponseEntity.ok(
                CursoMapper.toResponseDto(curso));
    }

    @PatchMapping("/descricao/{id}")
    public ResponseEntity<CursoResponseDto> atualizarDescricao(@PathVariable Long id, @RequestBody String novaDescricao){
        Curso curso = cursoService.atualizarDescricaoDoCurso(id, novaDescricao);
        return ResponseEntity.ok(
                CursoMapper.toResponseDto(curso));
    }

    @PatchMapping("/preco/{id}")
    public ResponseEntity<CursoResponseDto> atualizarDescricao(@PathVariable Long id, @RequestBody Double novoPreco){
        Curso curso = cursoService.atualizarPreco(id, novoPreco);
        return ResponseEntity.ok(
                CursoMapper.toResponseDto(curso));
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> deletarCursoPorId(@PathVariable Long id){
        cursoService.deletarCursoPorId(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/titulo")
    public ResponseEntity<Void> deletarCursoPorTitulo(@PathVariable String titulo){
        cursoService.deletarCursoPorTitulo(titulo);
        return ResponseEntity.ok().build();
    }

}
