package com.projeto.tcc.dto;

public record UserUpdateRequestDTO(
	    String name,
	    String email,
	    String password // Pode ser null se a senha não for alterada
	) {}