package com.cursos_online.usuario_service.mapper;

import com.cursos_online.usuario_service.dto.UsuarioRequestDto;
import com.cursos_online.usuario_service.dto.UsuarioResponseDto;
import com.cursos_online.usuario_service.dto.UsuarioAtualizarDto;
import com.cursos_online.usuario_service.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequestDto dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setTipo(dto.getTipo());

        return usuario;
    }

    public static Usuario toEntity(UsuarioAtualizarDto dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setTipo(dto.getTipo());

        return usuario;
    }

    public static UsuarioResponseDto toResponseDto(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTipo(usuario.getTipo());

        return dto;
    }
}
