package com.projeto.tcc.dto;

import com.projeto.tcc.domain.feedback.Feedback;
import java.time.LocalDateTime;

public record FeedbackResponseDTO(Long id, String texto, String mentorNome, LocalDateTime dataEnvio, boolean lido) {
    public FeedbackResponseDTO(Feedback feedback) {
        this(feedback.getId(), feedback.getTexto(), feedback.getMentor().getName(), feedback.getDataEnvio(), feedback.isLido());
    }
}