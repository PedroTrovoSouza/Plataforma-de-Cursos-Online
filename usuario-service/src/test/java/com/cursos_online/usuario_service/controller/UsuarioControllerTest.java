
package com.cursos_online.usuario_service.controller;

import com.cursos_online.usuario_service.dto.UsuarioAtualizarDto;
import com.cursos_online.usuario_service.dto.UsuarioRequestDto;
import com.cursos_online.usuario_service.entity.Usuario;
import com.cursos_online.usuario_service.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        usuarioRepository.deleteAll();
    }

    @Test
    public void testCadastrarUsuario() throws Exception {
        UsuarioRequestDto dto = new UsuarioRequestDto("João", "joao@example.com", "senha123", "ALUNO");

        mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.email").value("joao@example.com"))
                .andExpect(jsonPath("$.tipo").value("ALUNO"));
    }

    @Test
    public void testListarTodosUsuarios() throws Exception {
        usuarioRepository.save(new Usuario(null, "Maria", "maria@example.com", "senha", "PROFESSOR"));

        mockMvc.perform(get("/usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("Maria"));
    }

    @Test
    public void testListarUsuarioPorId() throws Exception {
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Carlos", "carlos@example.com", "senha", "ALUNO"));

        mockMvc.perform(get("/usuario/" + usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos"));
    }

    @Test
    public void testAtualizarUsuario() throws Exception {
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Ana", "ana@example.com", "senha", "ALUNO"));

        UsuarioAtualizarDto dto = new UsuarioAtualizarDto(usuario.getId(), "Ana Paula", "ana@example.com", "novaSenha", "PROFESSOR");

        mockMvc.perform(put("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Paula"))
                .andExpect(jsonPath("$.tipo").value("PROFESSOR"));
    }

    @Test
    public void testDeletarUsuario() throws Exception {
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Pedro", "pedro@example.com", "senha", "ALUNO"));

        mockMvc.perform(delete("/usuario/" + usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário deletado com sucesso."));
    }

    @Test
    public void testDeletarUsuarioNaoExistente() throws Exception {
        mockMvc.perform(delete("/usuario/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado."));
    }
}
