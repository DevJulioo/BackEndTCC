package com.projeto.tcc.domain.avaliacao;

import com.projeto.tcc.domain.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private User aluno;

    @Column(nullable = false)
    private Double nota;

    @Column(nullable = false)
    private LocalDateTime dataRealizacao = LocalDateTime.now();

    private String cursoId; // Ex: "iot", "ciberseguranca", etc.

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getAluno() { return aluno; }
    public void setAluno(User aluno) { this.aluno = aluno; }
    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }
    public LocalDateTime getDataRealizacao() { return dataRealizacao; }
    public void setDataRealizacao(LocalDateTime dataRealizacao) { this.dataRealizacao = dataRealizacao; }
    public String getCursoId() { return cursoId; }
    public void setCursoId(String cursoId) { this.cursoId = cursoId; }
}