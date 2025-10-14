package com.projeto.tcc.domain.user;

import com.projeto.tcc.domain.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity(name = "reunioes")
@Table(name = "reunioes")
public class Reuniao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private User aluno;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    private String assunto;
    private String observacoes;
    private String modalidade;
    private LocalDate data;
    private LocalTime hora;
    private String status;

    // Construtor vazio (obrigatório para JPA)
    public Reuniao() {
    }

    // --- Getters e Setters ---

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getAluno() { return aluno; }
    public void setAluno(User aluno) { this.aluno = aluno; }

    public User getMentor() { return mentor; }
    public void setMentor(User mentor) { this.mentor = mentor; }

    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getModalidade() { return modalidade; }
    public void setModalidade(String modalidade) { this.modalidade = modalidade; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}