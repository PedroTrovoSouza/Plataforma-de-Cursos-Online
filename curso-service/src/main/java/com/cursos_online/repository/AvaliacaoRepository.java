package com.cursos_online.repository;

import com.cursos_online.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findAllByIdUsuario(Long idUsuario);

    List<Avaliacao> findAllByCursos_Id(Long cursosId);
}
