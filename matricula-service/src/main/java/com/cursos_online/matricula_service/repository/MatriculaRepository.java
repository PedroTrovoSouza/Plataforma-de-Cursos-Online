package com.cursos_online.matricula_service.repository;

import com.cursos_online.matricula_service.dto.AlunoDTO;
import com.cursos_online.matricula_service.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    @Query("""
                SELECT new com.cursos_online.matricula_service.dto.AlunoDTO(
                    m.usuario.nome,
                    m.usuario.email
                )
                FROM Matricula m
                WHERE m.idCurso = :idCurso
            """)
    List<AlunoDTO> buscarAlunosPorCurso(@Param("idCurso") Long idCurso);
}
