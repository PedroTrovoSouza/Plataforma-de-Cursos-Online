package com.cursos.controller;

import com.cursos.dto.curso.CursoRequestDto;
import com.cursos.dto.curso.CursoCadastroDto;
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
    public ResponseEntity<List<CursoCadastroDto>> listarCursosCadastrados(){
        List<Curso> cursos = cursoService.listarTodosCursos();
        List<CursoCadastroDto> response = cursos.stream().map(CursoMapper::toResponseDto).toList();
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CursoCadastroDto> cadastrarNovoCurso(@RequestBody CursoRequestDto cursoParaCadastrar){
        Curso curso = CursoMapper.toEntity(cursoParaCadastrar);
        return ResponseEntity.status(201).body(CursoMapper
                .toResponseDto(cursoService
                        .cadastrarNovoCurso(curso)));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CursoCadastroDto> buscarCursoPorId(@PathVariable Long id){
        Curso curso = cursoService.buscarCursoPorId(id);
        CursoCadastroDto response = CursoMapper.toResponseDto(curso);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<CursoCadastroDto>> listarPorCategoria(@PathVariable String categoria){
        List<Curso> cursos = cursoService.listarPorCategoria(categoria);
        List<CursoCadastroDto> response = cursos.stream().map(CursoMapper::toResponseDto).toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/titulo/{id}")
    public ResponseEntity<CursoCadastroDto> atualizarTitulo(@PathVariable Long id, @RequestBody String novoTitulo){
        Curso curso = cursoService.atualizarTituloDoCurso(id, novoTitulo);
        return ResponseEntity.ok(
                CursoMapper.toResponseDto(curso));
    }

    @PatchMapping("/descricao/{id}")
    public ResponseEntity<CursoCadastroDto> atualizarDescricao(@PathVariable Long id, @RequestBody String novaDescricao){
        Curso curso = cursoService.atualizarDescricaoDoCurso(id, novaDescricao);
        return ResponseEntity.ok(
                CursoMapper.toResponseDto(curso));
    }

    @PatchMapping("/categoria/{id}")
    public ResponseEntity<CursoCadastroDto> atualizarCategoria(@PathVariable Long id, @RequestBody String novaCategoria){
        Curso curso = cursoService.atualizarCategoria(id, novaCategoria);
        return ResponseEntity.ok(
                CursoMapper.toResponseDto(curso));
    }

    @PatchMapping("/preco/{id}")
    public ResponseEntity<CursoCadastroDto> atualizarPreco(@PathVariable Long id, @RequestBody Double novoPreco){
        Curso curso = cursoService.atualizarPreco(id, novoPreco);
        return ResponseEntity.ok(
                CursoMapper.toResponseDto(curso));
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> deletarCursoPorId(@PathVariable Long id){
        cursoService.deletarCursoPorId(id);
        return ResponseEntity.ok().build();
    }

}
