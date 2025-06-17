package com.cursos_online.usuario_service.repository;

import com.cursos_online.usuario_service.entity.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
}
