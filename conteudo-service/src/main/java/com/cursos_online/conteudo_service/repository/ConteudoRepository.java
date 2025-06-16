package com.cursos_online.conteudo_service.repository;

import com.cursos_online.conteudo_service.entity.Conteudo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConteudoRepository extends JpaRepository<Conteudo, Long> {
    List<Conteudo> findByCursoId(Long cursoId);
}


