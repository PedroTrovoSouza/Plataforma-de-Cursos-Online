//package com.cursos_online.usuario_service.serviceTest;
//
//import com.cursos_online.conteudo_service.dto.AtualizarConteudoDTO;
//import com.cursos_online.conteudo_service.dto.CadastrarConteudoDTO;
//import com.cursos_online.conteudo_service.entity.Conteudo;
//import com.cursos_online.conteudo_service.repository.ConteudoRepository;
//import com.cursos_online.conteudo_service.service.ConteudoService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class ConteudoServiceTest {
//
//    @Mock
//    private ConteudoRepository conteudoRepository;
//
//    @InjectMocks
//    private ConteudoService conteudoService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        conteudoService = new ConteudoService(conteudoRepository);
//    }
//
//    @Test
//    public void testListarTodos() {
//        Conteudo conteudo1 = new Conteudo(1L, "Titulo 1", "url1", 1L);
//        Conteudo conteudo2 = new Conteudo(2L, "Titulo 2", "url2", 1L);
//        when(conteudoRepository.findAll()).thenReturn(Arrays.asList(conteudo1, conteudo2));
//
//        List<Conteudo> lista = conteudoService.listarTodos();
//        assertEquals(2, lista.size());
//        verify(conteudoRepository).findAll();
//    }
//
//    @Test
//    public void testBuscarPorIdFound() {
//        Conteudo conteudo = new Conteudo(1L, "Titulo", "url", 1L);
//        when(conteudoRepository.findById(1L)).thenReturn(Optional.of(conteudo));
//
//        Conteudo result = conteudoService.buscarPorId(1L);
//        assertNotNull(result);
//        assertEquals("Titulo", result.getTitulo());
//        verify(conteudoRepository).findById(1L);
//    }
//
//    @Test
//    public void testBuscarPorIdNotFound() {
//        when(conteudoRepository.findById(1L)).thenReturn(Optional.empty());
//        Exception ex = assertThrows(RuntimeException.class, () -> conteudoService.buscarPorId(1L));
//        assertTrue(ex.getMessage().contains("Conteúdo não encontrado"));
//    }
//
//    @Test
//    public void testSalvar() {
//        CadastrarConteudoDTO dto = new CadastrarConteudoDTO("Novo Conteúdo", "urlNovo", null);
//        Conteudo conteudoParaSalvar = new Conteudo(null, "Novo Conteúdo", "urlNovo", null);
//        Conteudo conteudoSalvo = new Conteudo(1L, "Novo Conteúdo", "urlNovo", null);
//
//        when(conteudoRepository.save(any(Conteudo.class))).thenReturn(conteudoSalvo);
//
//        Conteudo result = conteudoService.salvar(dto);
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//        verify(conteudoRepository).save(any(Conteudo.class));
//    }
//
//    @Test
//    public void testAtualizar() {
//        AtualizarConteudoDTO atualizarDTO = new AtualizarConteudoDTO("Titulo Atualizado");
//        Conteudo conteudoExistente = new Conteudo(1L, "Titulo Antigo", "url", 1L);
//        Conteudo conteudoAtualizado = new Conteudo(1L, "Titulo Atualizado", "url", 1L);
//
//        when(conteudoRepository.findById(1L)).thenReturn(Optional.of(conteudoExistente));
//        when(conteudoRepository.save(conteudoExistente)).thenReturn(conteudoAtualizado);
//
//        Conteudo result = conteudoService.atualizar(1L, atualizarDTO);
//        assertNotNull(result);
//        assertEquals("Titulo Atualizado", result.getTitulo());
//        verify(conteudoRepository).findById(1L);
//        verify(conteudoRepository).save(conteudoExistente);
//    }
//
//    @Test
//    public void testExcluir() {
//        doNothing().when(conteudoRepository).deleteById(1L);
//        conteudoService.excluir(1L);
//        verify(conteudoRepository).deleteById(1L);
//    }
//
//    @Test
//    public void testBuscarPorCursoId_ThrowsException() {
//        Exception ex = assertThrows(RuntimeException.class, () -> conteudoService.buscarPorCursoId(1L));
//        assertNotNull(ex.getMessage());
//    }
//}