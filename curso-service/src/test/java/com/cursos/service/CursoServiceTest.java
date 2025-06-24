package com.cursos.service;

import com.cursos.entity.Avaliacao;
import com.cursos.entity.Curso;
import com.cursos.exception.CursoConflitoException;
import com.cursos.repository.CursoRepository;
import kotlin.collections.EmptyList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

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

    @Test
    void deveCadastrarNovoCursoComSucesso(){
        //Given

        //When
        when(cursoRepository.existsByTitulo(anyString())).thenReturn(false);
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        //Then
        Curso resultado = cursoService.cadastrarNovoCurso(curso);

        //Assert
        assertNotNull(resultado);
        assertEquals(resultado.getId(), curso.getId());
        assertEquals(resultado.getNota(), curso.getNota());
        verify(cursoRepository, times(1)).save(curso);
    }

    @Test
    void deveLancarExcessaoAoTentarCadastrarCursoComTituloIgual(){
        //Given

        //When
        when(cursoRepository.existsByTitulo(anyString())).thenReturn(true);

        //Then

        //Assert
        assertThrows(CursoConflitoException.class, () -> cursoService.cadastrarNovoCurso(curso));
    }

    @Test
    void deveRetornarListaComUmCurso(){
        //Given

        //When
        when(cursoRepository.findAll()).thenReturn(List.of(curso));

        //Then
        List<Curso> resultado = cursoService.listarTodosCursos();

        //Assert
        assertEquals(1, resultado.size());
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVazia(){
        //Given

        //When
        when(cursoRepository.findAll()).thenReturn(Collections.emptyList());

        //Then
        List<Curso> resultado = cursoService.listarTodosCursos();

        //Assert
        assertEquals(0, resultado.size());
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void deveAtualiazarCursoComNovoNome(){
        //Given
        String novoTitulo = "Novo Titulo";
        Curso cursoAtualizado = new Curso("Novo Titulo", "Aprenda a como ficar milionario em 3 dias com" +
                " este curso incrivel", "Dinheiro", 799.90, 3.0, avaliacoes);

        //When
        when(cursoRepository.existsByTitulo(anyString())).thenReturn(false);
        when(cursoRepository.findById(anyLong())).thenReturn(Optional.of(curso));
        when(cursoRepository.save(curso)).thenReturn(cursoAtualizado);

        //Then
        Curso resultado = cursoService.atualizarTituloDoCurso(anyLong(), novoTitulo);

        //Assert
        assertNotNull(resultado);
        assertEquals(novoTitulo, resultado.getTitulo());
        verify(cursoRepository, times(1)).save(cursoAtualizado);
    }

    @Test
    void deveDeletarCursoComIdInformado(){
        when(cursoRepository.findById(any())).thenReturn(Optional.of(curso));
        doNothing().when(cursoRepository).delete(curso);
        cursoService.deletarCursoPorId(1L);
        verify(cursoRepository).delete(curso);
    }

    @Test
    void deveAtualizarNotaDoCursoComSucesso(){
        //Given
        Avaliacao avaliacao4 = new Avaliacao(4L, 5.0, "muito dinheiro", 4L, "Pedro", curso);
        avaliacoes.add(avaliacao4);

        Curso cursoComNovaNota = curso;
        cursoComNovaNota.setAvaliacoes(avaliacoes);

        //When
        when(cursoRepository.findById(anyLong())).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenReturn(cursoComNovaNota);

        //Then
        cursoService.atualizarNotaDoCurso(anyLong(), avaliacoes);
        verify(cursoRepository).save(cursoComNovaNota);
    }

}