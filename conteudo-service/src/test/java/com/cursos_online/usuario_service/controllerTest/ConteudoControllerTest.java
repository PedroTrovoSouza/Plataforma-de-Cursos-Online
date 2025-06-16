package com.cursos_online.usuario_service.controllerTest;

import com.cursos_online.conteudo_service.controller.ConteudoController;
import com.cursos_online.conteudo_service.dto.AtualizarConteudoDTO;
import com.cursos_online.conteudo_service.dto.CadastrarConteudoDTO;
import com.cursos_online.conteudo_service.entity.Conteudo;
import com.cursos_online.conteudo_service.service.ConteudoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConteudoController.class)
public class ConteudoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConteudoService conteudoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testBuscarConteudosPorCurso() throws Exception {
        Conteudo conteudo1 = new Conteudo(1L, "Titulo 1", "url1", 1L);
        Conteudo conteudo2 = new Conteudo(2L, "Titulo 2", "url2", 1L);
        Mockito.when(conteudoService.buscarPorCursoId(1L))
                .thenReturn(Arrays.asList(conteudo1, conteudo2));

        mockMvc.perform(get("/conteudo/curso/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Titulo 1"))
                .andExpect(jsonPath("$[0].url_video").value("url1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].titulo").value("Titulo 2"))
                .andExpect(jsonPath("$[1].url_video").value("url2"));
    }

    @Test
    public void testBuscarConteudoPorId() throws Exception {
        Conteudo conteudo = new Conteudo(1L, "Titulo", "url", 1L);
        Mockito.when(conteudoService.buscarPorId(1L)).thenReturn(conteudo);

        mockMvc.perform(get("/conteudo/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Titulo"))
                .andExpect(jsonPath("$.url_video").value("url"));
    }

    @Test
    public void testSalvarConteudo() throws Exception {
        CadastrarConteudoDTO cadastrarDTO = new CadastrarConteudoDTO("Novo Conteudo", "urlNovo");
        Conteudo conteudoSalvo = new Conteudo(1L, "Novo Conteudo", "urlNovo", null);
        Mockito.when(conteudoService.salvar(any(CadastrarConteudoDTO.class))).thenReturn(conteudoSalvo);

        mockMvc.perform(post("/conteudo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastrarDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Novo Conteudo"))
                .andExpect(jsonPath("$.url_video").value("urlNovo"));
    }

    @Test
    public void testAtualizarConteudo() throws Exception {
        AtualizarConteudoDTO atualizarDTO = new AtualizarConteudoDTO("Titulo Atualizado");
        Conteudo conteudoAtualizado = new Conteudo(1L, "Titulo Atualizado", "url", 1L);
        Mockito.when(conteudoService.atualizar(eq(1L), any(AtualizarConteudoDTO.class)))
                .thenReturn(conteudoAtualizado);

        mockMvc.perform(put("/conteudo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizarDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Titulo Atualizado"))
                .andExpect(jsonPath("$.url_video").value("url"));
    }

    @Test
    public void testExcluirConteudo() throws Exception {
        mockMvc.perform(delete("/conteudo/1"))
                .andExpect(status().isNoContent());
    }
}