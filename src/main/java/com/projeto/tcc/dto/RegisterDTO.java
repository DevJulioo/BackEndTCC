package com.projeto.tcc.dto;

import com.projeto.tcc.domain.user.UserRoles;

public record RegisterDTO(
    String name, 
    String email, 
    String password, 
    UserRoles role,
    Integer dailyLearningGoalMinutes // --- CAMPO ADICIONADO ---
) {}