package com.projeto.tcc.domain.feedback;

import com.projeto.tcc.domain.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A anotação aqui estava FetchType.LAZY (padrão)
    // Mudamos para EAGER para carregar o aluno junto
    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "aluno_id", nullable = false)
    private User aluno;

    // A anotação aqui também foi mudada para EAGER
    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String texto;

    @Column(nullable = false)
    private boolean lido = false;

    @Column(nullable = false)
    private LocalDateTime dataEnvio = LocalDateTime.now();
    
    public Feedback() {
        this.dataEnvio = LocalDateTime.now();
    }

    // Getters e Setters (não precisam de alteração)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getAluno() { return aluno; }
    public void setAluno(User aluno) { this.aluno = aluno; }
    public User getMentor() { return mentor; }
    public void setMentor(User mentor) { this.mentor = mentor; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public boolean isLido() { return lido; }
    public void setLido(boolean lido) { this.lido = lido; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }

}