package com.projeto.tcc.dto;

import com.projeto.tcc.domain.user.UserRoles;

public record RegisterRequestDTO(
    String name, 
    String email, 
    String password, 
    UserRoles role,
    String mentorCode, // Mantido para cadastro de mentor
    Integer dailyLearningGoalMinutes // --- CAMPO ADICIONADO ---
) {}