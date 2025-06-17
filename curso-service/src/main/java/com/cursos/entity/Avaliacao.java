package com.cursos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double nota;

    private String comentario;

    private Long idUsuario;

    private String nomeUsuario;

    @ManyToOne
    private Curso curso;

    public Avaliacao(Double nota, String comentario, Long idUsuario) {
        this.nota = nota;
        this.comentario = comentario;
        this.idUsuario = idUsuario;
    }
}
