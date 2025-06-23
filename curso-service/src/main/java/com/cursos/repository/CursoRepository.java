package com.cursos.repository;

import com.cursos.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByTitulo(String titulo);

    boolean existsByDescricao(String descricao);

    Curso findByTitulo(String titulo);

    Curso findByTituloContaining(String titulo);

    List<Curso> findAllByCategoriaContaining(String categoria);
}
