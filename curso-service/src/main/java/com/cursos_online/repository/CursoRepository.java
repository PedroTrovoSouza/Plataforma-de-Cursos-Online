package com.cursos_online.repository;

import com.cursos_online.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByTituloOrDescricaoAllIgnoreCase(String titulo, String descricao);

    boolean existsByTitulo(String titulo);

    boolean existsByDescricao(String descricao);

    Optional<Curso> findByTitulo(String titulo);
}
