package com.cursos_online.matricula_service.controller;

import com.cursos_online.matricula_service.dto.AlunoDTO;
import com.cursos_online.matricula_service.dto.MatriculaResponseDTO;
import com.cursos_online.matricula_service.dto.UsuarioDTO;
import com.cursos_online.matricula_service.entity.Matricula;
import com.cursos_online.matricula_service.service.MatriculaService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matricula")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @PostMapping("/cadastrar/{nome}")
    public ResponseEntity<?> cadastrarMatricula(@PathVariable String nome, @RequestBody String email) {
        try {
            MatriculaResponseDTO matriculaSalva = matriculaService.cadastrarMatricula(nome, email);
            return ResponseEntity.status(201).body(matriculaSalva);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> buscarTodasMatriculas() {
        try {
            List<MatriculaResponseDTO> matriculasEncontradas = matriculaService.buscarMatricula();
            return ResponseEntity.ok().body(matriculasEncontradas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/buscar-matriculas/{id}")
    public ResponseEntity<?> buscarAlunosMatriculados(@PathVariable long id) {
        try {
            List<UsuarioDTO> alunosEncontrados = matriculaService.buscarUsuariosMatriculados(id);
            return ResponseEntity.ok().body(alunosEncontrados);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/buscar-dados-matricula/{id}")
    public ResponseEntity<?> buscarDadosMatricula(@PathVariable long id){
        try {
            Matricula matriculaEncontrada = matriculaService.buscarDadosMatricula(id);
            return ResponseEntity.ok().body(matriculaEncontrada);
        }catch (RuntimeException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/cancelar-matricula/{id}")
    public ResponseEntity<?> cancelarMatricula(@PathVariable long id) {
        try {
            matriculaService.cancelarMatricula(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
