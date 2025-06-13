package com.cursos_online.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double nota;

    private String comentario;

    private Long idUsuario;

    @ManyToMany(mappedBy = "avaliacoes")
    private List<Curso> cursos;
}
