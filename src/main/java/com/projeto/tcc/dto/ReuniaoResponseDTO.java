package com.projeto.tcc.dto;

import com.projeto.tcc.domain.user.Reuniao;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ReuniaoResponseDTO(
    UUID id,
    String assunto,
    LocalDate data,
    LocalTime hora,
    String modalidade,
    String status,
    MentorDTO aluno,
    MentorDTO mentor,
    String observacoes // <-- 1. CAMPO ADICIONADO AQUI
) {
    public ReuniaoResponseDTO(Reuniao reuniao) {
        this(
            reuniao.getId(),
            reuniao.getAssunto(),
            reuniao.getData(),
            reuniao.getHora(),
            reuniao.getModalidade(),
            reuniao.getStatus(),
            new MentorDTO(reuniao.getAluno().getId(), reuniao.getAluno().getName()),
            new MentorDTO(reuniao.getMentor().getId(), reuniao.getMentor().getName()),
            reuniao.getObservacoes() // <-- 2. MAPEAMENTO ADICIONADO AQUI
        );
    }
}