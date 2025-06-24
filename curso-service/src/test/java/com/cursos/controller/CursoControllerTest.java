package com.cursos.controller;

import com.cursos.dto.curso.CursoRequestDto;
import com.cursos.dto.curso.CursoResponseDto;
import com.cursos.entity.Avaliacao;
import com.cursos.entity.Curso;
import com.cursos.mapper.CursoMapper;
import com.cursos.repository.CursoRepository;
import com.cursos.service.AvaliacaoService;
import com.cursos.service.CursoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Curso curso;

    private Avaliacao avaliacao1;

    private Avaliacao avaliacao2;

    private Avaliacao avaliacao3;

    private List<Avaliacao> avaliacoes;

    @BeforeEach
    void setUp(){
        avaliacoes = new ArrayList<>();
        avaliacao1 = new Avaliacao(1L, 5.0, "Curso muito bom eu ganhei 100 reais em uma semana", 1L, "Fernando", curso);
        avaliacoes.add(avaliacao1);
        avaliacao2 = new Avaliacao(2L, 2.0, "Nao gostei, ainda nao ganhei nada em 2 meses", 2L, "Paulo", curso);
        avaliacoes.add(avaliacao2);
        avaliacao3 = new Avaliacao(3L, 0.5, "Faz 1 ano que eu nao ganho nada", 3L, "Ricardo", curso);
        avaliacoes.add(avaliacao3);
        curso = new Curso("Como ficar rico Vendendo cursos online", "Aprenda a como ficar milionario em 3 dias com este curso incrivel", "Dinheiro", 799.90, 3.0, avaliacoes);
    }

    @AfterEach
    void limparBanco() {
        cursoRepository.deleteAll();
    }

    @Test
    void deveRetornarCodigo200EListarCurso() throws Exception {
        Curso cursoCadastrado = cursoService.cadastrarNovoCurso(curso);

        CursoResponseDto cursoResponseDto = CursoMapper.toResponseDto(cursoCadastrado);

        mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value(cursoResponseDto.getTitulo()))
                .andExpect(jsonPath("$[0].descricao").value(cursoResponseDto.getDescricao()))
                .andExpect(jsonPath("$[0].nota").value(cursoResponseDto.getNota()));
    }

    @Test
    void deveRetornarCodigo204EListaVazia() throws Exception {
        mockMvc.perform(get("/cursos"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void deveCadastrarNovoCursoComSucesso() throws Exception {
        CursoRequestDto cursoRequestDto = new CursoRequestDto(curso.getTitulo(), curso.getDescricao(), curso.getCategoria(), curso.getPreco());

        mockMvc.perform(post("/cursos").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value(curso.getTitulo()));
    }

    @Test
    void deveBuscarCursoPorIdInformadoERetornarCodigo200() throws Exception {
    Curso cursoCadastrado = cursoService.cadastrarNovoCurso(curso);

        mockMvc.perform(get("/cursos/id/{id}", cursoCadastrado.getId()))
                .andExpect(jsonPath("$.titulo").value(curso.getTitulo()));
    }

//    @Test
//    void deveAtualizarDescricaoDoCursoComIdInformado() throws Exception {
//        avaliacaoService.avaliarCurso(avaliacao1, curso.getId());
//        avaliacaoService.avaliarCurso(avaliacao2, curso.getId());
//        avaliacaoService.avaliarCurso(avaliacao3, curso.getId());
//        Curso cursoCadastrado = cursoService.cadastrarNovoCurso(curso);
//        String novaDescricao = "Nova Descricao";
//
//        mockMvc.perform(patch("/cursos/descricao/{id}", cursoCadastrado.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                .content("\"Nova Descricao\""))
//                .andExpect(jsonPath("$.descricao").value(novaDescricao));
//    }

    @Test
    void deveDeletarCursoComSucesso() throws Exception {
        // Arrange: cria e salva um curso
        Curso cursoSalvo = cursoService.cadastrarNovoCurso(curso);

        Long id = cursoSalvo.getId();

        // Act + Assert: chama o endpoint DELETE
        mockMvc.perform(delete("/cursos/{id}", id))
                .andExpect(status().isOk());

        // Assert: verifica que o curso não existe mais
        boolean existe = cursoRepository.findById(id).isPresent();
        assertFalse(existe, "O curso deveria ter sido deletado");
    }


}