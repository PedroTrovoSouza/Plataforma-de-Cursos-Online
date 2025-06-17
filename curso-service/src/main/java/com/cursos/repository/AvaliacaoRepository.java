package com.cursos.repository;

import com.cursos.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findAllByCursoId(Long cursoId);

    List<Avaliacao> findAllByIdUsuario(Long idUsuario);
}
