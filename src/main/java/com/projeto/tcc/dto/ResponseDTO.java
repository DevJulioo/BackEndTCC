package com.projeto.tcc.dto;

import com.projeto.tcc.domain.user.UserRoles;

public record ResponseDTO (String name, String token, UserRoles role){
}
