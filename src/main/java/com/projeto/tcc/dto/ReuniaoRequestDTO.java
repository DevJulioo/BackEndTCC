package com.projeto.tcc.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReuniaoRequestDTO(
    String mentorId, // Corrigido para String
    String assunto,
    String observacoes,
    String modalidade,
    LocalDate data,
    LocalTime hora
) {
}