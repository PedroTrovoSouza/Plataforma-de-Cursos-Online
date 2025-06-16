package com.cursos_online.usuario_service.service;

import com.cursos_online.usuario_service.dto.UsuarioAtualizarDto;
import com.cursos_online.usuario_service.dto.UsuarioEmailEventoDto;
import com.cursos_online.usuario_service.dto.UsuarioRequestDto;
import com.cursos_online.usuario_service.dto.UsuarioResponseDto;
import com.cursos_online.usuario_service.entity.Usuario;
import com.cursos_online.usuario_service.mapper.UsuarioMapper;
import com.cursos_online.usuario_service.messaging.producer.UsuarioProducer;
import com.cursos_online.usuario_service.repository.UsuarioRepository;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioProducer usuarioProducer;

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    public UsuarioResponseDto cadastrarUsuario(UsuarioRequestDto usuarioRequestDto) {
        Usuario usuario = UsuarioMapper.toEntity(usuarioRequestDto);

        if (usuario == null) {
            throw new IllegalArgumentException("Dados do usuário inválidos");
        }

        if (!(usuario.getTipo().equalsIgnoreCase("PROFESSOR") ||
            usuario.getTipo().equalsIgnoreCase("ALUNO"))) {
            throw new IllegalArgumentException("Tipo de usuário inválido. Deve ser 'PROFESSOR' ou 'ALUNO'.");
        }

        if(usuario.getTipo().equalsIgnoreCase("PROFESSOR")) {
            usuario.setTipo("PROFESSOR");
        }

        if(usuario.getTipo().equalsIgnoreCase("ALUNO")) {
            usuario.setTipo("ALUNO");
        }

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }

        usuarioRepository.save(usuario);

        UsuarioEmailEventoDto eventoDto = new UsuarioEmailEventoDto(usuario.getNome(), usuario.getEmail());
        usuarioProducer.enviarEventoUsuarioCriado(eventoDto);

        UsuarioResponseDto responseDto = UsuarioMapper.toResponseDto(usuario);
        return responseDto;
    }

    public List<UsuarioResponseDto> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            return null;
        }
        return usuarios.stream()
                .map(UsuarioMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDto listarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return null;
        }
        return UsuarioMapper.toResponseDto(usuario);
    }

    public UsuarioResponseDto atualizarUsuario(UsuarioAtualizarDto usuarioRequestDto) {
        Usuario usuarioExistente = usuarioRepository.findById(usuarioRequestDto.getId()).orElse(null);
        if (usuarioExistente == null) {
            return null;
        }

        Usuario usuarioAtualizado = UsuarioMapper.toEntity(usuarioRequestDto);
        if (usuarioAtualizado == null) {
            throw new IllegalArgumentException("Dados do usuário inválidos");
        }

        if (!(usuarioAtualizado.getTipo().equalsIgnoreCase("PROFESSOR") ||
            usuarioAtualizado.getTipo().equalsIgnoreCase("ALUNO"))) {
            throw new IllegalArgumentException("Tipo de usuário inválido. Deve ser 'PROFESSOR' ou 'ALUNO'.");
        }

        if(usuarioAtualizado.getTipo().equalsIgnoreCase("PROFESSOR")) {
            usuarioAtualizado.setTipo("PROFESSOR");
        }

        if(usuarioAtualizado.getTipo().equalsIgnoreCase("ALUNO")) {
            usuarioAtualizado.setTipo("ALUNO");
        }

        usuarioAtualizado.setId(usuarioExistente.getId());
        usuarioRepository.save(usuarioAtualizado);

        return UsuarioMapper.toResponseDto(usuarioAtualizado);
    }

    public boolean deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            return false;
        }
        usuarioRepository.deleteById(id);
        return true;
    }

    public UsuarioResponseDto listarUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmailEqualsIgnoreCase(email).orElse(null);
        if (usuario == null) {
            return null;
        }
        return UsuarioMapper.toResponseDto(usuario);
    }
}
