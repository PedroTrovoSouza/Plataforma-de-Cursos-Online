package com.cursos_online.usuario_service.controller;

import com.cursos_online.usuario_service.dto.*;
import com.cursos_online.usuario_service.entity.Usuario;
import com.cursos_online.usuario_service.repository.UsuarioRepository;
import com.cursos_online.usuario_service.security.JwtUtil;
import com.cursos_online.usuario_service.service.UsuarioService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioService service;

    private final JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository repository;

    public UsuarioController(UsuarioService service, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
        Usuario usuario = service.autenticarUsuario(loginRequest);

        if (usuario == null) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        String token = jwtUtil.generateToken(usuario.getEmail());
        return ResponseEntity.ok(token);
    }

@PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDto> cadastrar(@RequestBody UsuarioRequestDto dto) {
        UsuarioResponseDto response = service.cadastrarUsuario(dto);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarTodos() {
        List<UsuarioResponseDto> response = service.listarTodosUsuarios();

        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> listarUsuarioPorId (@PathVariable Long id) {
        UsuarioResponseDto usuarioPorId = service.listarUsuarioPorId(id);

        if (usuarioPorId == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(usuarioPorId);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> listarUsuarioPorEmail(@PathVariable String email) {
        UsuarioDTO usuarioPorEmail = service.listarUsuarioPorEmail(email);

        if (usuarioPorEmail == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(usuarioPorEmail);
    }

    @PutMapping
    public ResponseEntity<UsuarioResponseDto> atualizarUsuario(@RequestBody UsuarioAtualizarDto dto) {
        UsuarioResponseDto response = service.atualizarUsuario(dto);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id) {
        boolean deletado = service.deletarUsuario(id);

        if (!deletado) {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }
        return ResponseEntity.status(200).body("Usuário deletado com sucesso.");
    }

    @GetMapping("listarInterno")
    public ResponseEntity<List<UsuarioDTO>> listarUsuariosParaServicos() {
        List<Usuario> usuarios = repository.findAll();
        List<UsuarioDTO> dtos = usuarios.stream()
                .map(usuario -> new UsuarioDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTipo()))
                .toList();

        return ResponseEntity.ok(dtos);
    }

}
