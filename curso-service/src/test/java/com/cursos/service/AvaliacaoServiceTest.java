package com.cursos.service;

import com.cursos.dto.usuario.UsuarioResponseDto;
import com.cursos.entity.Avaliacao;
import com.cursos.entity.Curso;
import com.cursos.exception.NotaDeAvaliacaoInvalida;
import com.cursos.repository.AvaliacaoRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvaliacaoServiceTest {

    private WireMockServer wireMockServer;
    private AvaliacaoRepository avaliacaoRepository;
    private CursoService cursoService;
    private AvaliacaoService avaliacaoService;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(8082);
        wireMockServer.start();
        configureFor("localhost", 8082);

        avaliacaoRepository = mock(AvaliacaoRepository.class);
        cursoService = mock(CursoService.class);
        WebClient.Builder builder = WebClient.builder();
        avaliacaoService = new AvaliacaoService(avaliacaoRepository, cursoService, builder);
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    @Test
    void deveCadastrarAvaliacaoComWireMock() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(9.0);
        avaliacao.setIdUsuario(1L);

        Curso curso = new Curso();
        curso.setId(100L);
        when(cursoService.buscarCursoPorId(100L)).thenReturn(curso);
        when(avaliacaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Stub do WireMock
        wireMockServer.stubFor(get(urlEqualTo("/usuario/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"nome\":\"Carlos\"}")
                        .withStatus(200)));

        Avaliacao resultado = avaliacaoService.avaliarCurso(avaliacao, 100L);

        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNomeUsuario());
        assertEquals(curso, resultado.getCurso());
        verify(avaliacaoRepository).save(avaliacao);
    }
}
