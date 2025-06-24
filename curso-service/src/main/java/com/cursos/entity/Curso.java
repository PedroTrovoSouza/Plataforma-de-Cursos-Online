package com.cursos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descricao;

    private String categoria;

    private Double preco;

    private Double nota;

    @ManyToMany
    @JoinTable(
            name = "curso_avaliacao",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "avaliacao_id")
    )
    private List<Avaliacao> avaliacoes;

    public Curso(String titulo, String descricao, String categoria, Double preco) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.preco = preco;
    }


    public void atualizarNota(List<Avaliacao> avaliacoes) {
        this.nota = avaliacoes.stream()
                .mapToDouble(Avaliacao::getNota)
                .average()
                .orElse(0.0);
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public Curso(String titulo, String descricao, String categoria, Double preco, Double nota, List<Avaliacao> avaliacoes) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.preco = preco;
        this.nota = nota;
        this.avaliacoes = avaliacoes;
    }
}
