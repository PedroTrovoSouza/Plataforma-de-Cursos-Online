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

    private String url_video;

    private Long cursoId;

    public Conteudo(String titulo, String url_video, Long cursoId) {
        this.titulo = titulo;
        this.url_video = url_video;
        this.cursoId = cursoId;
    }
}