package com.cursos_online.usuario_service.service;// src/test/java/com/cursos_online/usuario_service/service/UsuarioServiceTest.java
import com.cursos_online.usuario_service.dto.*;
import com.cursos_online.usuario_service.entity.Usuario;
import com.cursos_online.usuario_service.mapper.UsuarioMapper;
import com.cursos_online.usuario_service.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // cadastrarUsuario - positivo
    @Test
    void deveCadastrarUsuarioComSucesso() {
        UsuarioRequestDto dto = new UsuarioRequestDto("Joao", "joao@email.com", "12345678", "ALUNO");
        Usuario usuario = UsuarioMapper.toEntity(dto);

        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDto response = usuarioService.cadastrarUsuario(dto);

        assertNotNull(response);
        assertEquals(usuario.getEmail(), response.getEmail());
    }

    // cadastrarUsuario - negativo (email já existe)
    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        UsuarioRequestDto dto = new UsuarioRequestDto("Joao", "joao@email.com", "12345678", "ALUNO");
        Usuario usuario = UsuarioMapper.toEntity(dto);

        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.cadastrarUsuario(dto));
    }

    // cadastrarUsuario - negativo (tipo inválido)
    @Test
    void deveLancarExcecaoQuandoTipoInvalido() {
        UsuarioRequestDto dto = new UsuarioRequestDto("Joao", "joao@email.com", "12345678", "ADMIN");
        Usuario usuario = UsuarioMapper.toEntity(dto);

        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.cadastrarUsuario(dto));
    }

    // listarTodosUsuarios - positivo
    @Test
    void deveListarTodosUsuariosComSucesso() {
        Usuario usuario = new Usuario(1L, "Joao", "joao@email.com", "12345678", "ALUNO");
        List<Usuario> usuarios = List.of(usuario);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<UsuarioResponseDto> response = usuarioService.listarTodosUsuarios();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(usuario.getEmail(), response.get(0).getEmail());
    }

    // listarTodosUsuarios - negativo (lista vazia)
    @Test
    void deveRetornarNullQuandoNaoExistemUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<UsuarioResponseDto> response = usuarioService.listarTodosUsuarios();

        assertNull(response);
    }

    // listarUsuarioPorId - positivo
    @Test
    void deveListarUsuarioPorIdComSucesso() {
        Usuario usuario = new Usuario(1L, "Joao", "joao@email.com", "12345678", "ALUNO");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponseDto response = usuarioService.listarUsuarioPorId(1L);

        assertNotNull(response);
        assertEquals(usuario.getEmail(), response.getEmail());
    }

    // listarUsuarioPorId - negativo (não encontrado)
    @Test
    void deveRetornarNullQuandoUsuarioNaoEncontradoPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        UsuarioResponseDto response = usuarioService.listarUsuarioPorId(1L);

        assertNull(response);
    }

    // atualizarUsuario - positivo
    @Test
    void deveAtualizarUsuarioComSucesso() {
        UsuarioAtualizarDto dto = new UsuarioAtualizarDto(1L, "Joao Atualizado", "joao@email.com", "12345678", "PROFESSOR");
        Usuario usuarioExistente = new Usuario(1L, "Joao", "joao@email.com", "12345678", "ALUNO");
        Usuario usuarioAtualizado = UsuarioMapper.toEntity(dto);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

        UsuarioResponseDto response = usuarioService.atualizarUsuario(dto);

        assertNotNull(response);
        assertEquals(dto.getNome(), response.getNome());
        assertEquals("PROFESSOR", response.getTipo());
    }

    // atualizarUsuario - negativo (usuário não existe)
    @Test
    void deveRetornarNullQuandoUsuarioParaAtualizarNaoExiste() {
        UsuarioAtualizarDto dto = new UsuarioAtualizarDto(1L, "Joao Atualizado", "joao@email.com", "12345678", "PROFESSOR");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        UsuarioResponseDto response = usuarioService.atualizarUsuario(dto);

        assertNull(response);
    }

    // atualizarUsuario - negativo (tipo inválido)
    @Test
    void deveLancarExcecaoAoAtualizarComTipoInvalido() {
        UsuarioAtualizarDto dto = new UsuarioAtualizarDto(1L, "Joao Atualizado", "joao@email.com", "12345678", "ADMIN");
        Usuario usuarioExistente = new Usuario(1L, "Joao", "joao@email.com", "12345678", "ALUNO");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));

        assertThrows(IllegalArgumentException.class, () -> usuarioService.atualizarUsuario(dto));
    }

    // deletarUsuario - positivo
    @Test
    void deveDeletarUsuarioComSucesso() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        boolean result = usuarioService.deletarUsuario(1L);

        assertTrue(result);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    // deletarUsuario - negativo (usuário não existe)
    @Test
    void deveRetornarFalseAoTentarDeletarUsuarioInexistente() {
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        boolean result = usuarioService.deletarUsuario(1L);

        assertFalse(result);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    //listarUsuarioPorEmail - positivo
    @Test
    void deveRetornarUsuarioQuandoEmailExistir() {
        // Arrange
        String email = "joao@email.com";
        Usuario usuario = new Usuario(1L, "João", email, "senha123", "ALUNO");

        when(usuarioRepository.findByEmailEqualsIgnoreCase(email)).thenReturn(Optional.of(usuario));

        // Act
        UsuarioDTO resultado = usuarioService.listarUsuarioPorEmail(email);

        // Assert
        assertNotNull(resultado);
        assertEquals("João", resultado.getNome());
        assertEquals(email, resultado.getEmail());
        verify(usuarioRepository).findByEmailEqualsIgnoreCase(email);
    }

    //listarUsuarioPorEmail - negativo
    @Test
    void deveRetornarNullQuandoEmailNaoExistir() {
        // Arrange
        String email = "naoexiste@email.com";

        when(usuarioRepository.findByEmailEqualsIgnoreCase(email)).thenReturn(Optional.empty());

        // Act
        UsuarioDTO resultado = usuarioService.listarUsuarioPorEmail(email);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByEmailEqualsIgnoreCase(email);
    }

}