package com.cursos_online.conteudo_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Conteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String urlConteudo;

    private Long cursoId;

    public Conteudo(String titulo, String urlConteudo, Long cursoId) {
        this.titulo = titulo;
        this.urlConteudo = urlConteudo;
        this.cursoId = cursoId;
    }
}