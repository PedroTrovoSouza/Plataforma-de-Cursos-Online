package com.cursos_online.matricula_service.repository;

import com.cursos_online.matricula_service.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
}
