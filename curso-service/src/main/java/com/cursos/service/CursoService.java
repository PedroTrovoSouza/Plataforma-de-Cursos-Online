package com.cursos.service;

import com.cursos.entity.Curso;
import com.cursos.exception.CursoConflitoException;
import com.cursos.exception.CursoNaoEncontradoException;
import com.cursos.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;

    public List<Curso> listarTodosCursos(){
        return cursoRepository.findAll();
    }

    public Curso cadastrarNovoCurso(Curso cursoParaCadastrar){
        if (cursoRepository.existsByTituloOrDescricaoAllIgnoreCase(cursoParaCadastrar.getTitulo(), cursoParaCadastrar.getDescricao())){
            throw new CursoConflitoException("Curso com Titulo ou Descrição ja cadastrado!");
        }
        return cursoRepository.save(cursoParaCadastrar);
    }

    public Curso buscarCursoPorId(Long id){
        return cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNaoEncontradoException("Curso com ID não encontrado!"));
    }

    public Curso atualizarTituloDoCurso(Long id, String novoTItulo){
        if(cursoRepository.existsByTitulo(novoTItulo)){
            throw new CursoConflitoException("Curso com Titulo identico ja cadastrado!");
        }
        Curso cursoParaAtualizar = buscarCursoPorId(id);
        cursoParaAtualizar.setTitulo(novoTItulo);
        return cursoRepository.save(cursoParaAtualizar);
    }

    public Curso atualizarDescricaoDoCurso(Long id, String novaDescricao){
        if(cursoRepository.existsByDescricao(novaDescricao)){
            throw new CursoConflitoException("Curso com Descrição identico ja cadastrado!");
        }
        Curso cursoParaAtualizar = buscarCursoPorId(id);
        cursoParaAtualizar.setDescricao(novaDescricao);
        return cursoRepository.save(cursoParaAtualizar);
    }

    public Curso atualizarCategoria(Long id, String novaCategoria){
        Curso cursoParaAtualizar = buscarCursoPorId(id);
        cursoParaAtualizar.setCategoria(novaCategoria);
        return cursoRepository.save(cursoParaAtualizar);
    }

    public Curso atualizarPreco(Long id, Double novoPreco){
        Curso cursoParaAtualizar = buscarCursoPorId(id);
        cursoParaAtualizar.setPreco(novoPreco);
        return cursoRepository.save(cursoParaAtualizar);
    }

    public void deletarCursoPorId(Long id){
        Curso cursoParaDeletar = buscarCursoPorId(id);
        cursoRepository.delete(cursoParaDeletar);
    }

    public void deletarCursoPorTitulo(String titulo){
        Curso cursoParaDeletar = cursoRepository.findByTitulo(titulo)
                .orElseThrow(() -> new CursoNaoEncontradoException("Curso com Título não encontrado."));
        cursoRepository.delete(cursoParaDeletar);
    }

    public Curso buscarCursoPorNome(String nome) {
        return cursoRepository.findByTitulo(nome)
                .orElseThrow(() -> new CursoNaoEncontradoException("Curso com ID não encontrado!"));
    }
}
