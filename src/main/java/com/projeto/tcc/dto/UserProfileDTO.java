package com.projeto.tcc.dto;

import com.projeto.tcc.domain.user.UserRoles;

public record UserProfileDTO(String name, String email, UserRoles role, String avatarUrl) {
}