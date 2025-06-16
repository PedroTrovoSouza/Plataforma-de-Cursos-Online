package com.cursos_online.matricula_service.repository;

import com.cursos_online.matricula_service.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    @Query("SELECT m.idUsuario FROM Matricula m WHERE m.idCurso = :idCurso")
    List<Long> buscarIdsUsuariosPorCurso(@Param("idCurso") Long idCurso);
}
