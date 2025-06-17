package com.cursos_online.usuario_service.repository;

import com.cursos_online.usuario_service.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Boolean existsByEmail(String email);

    Optional<Usuario> findByEmailEqualsIgnoreCase(String email);

    Optional<Usuario> findByEmail(String username);
}
