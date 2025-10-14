package com.projeto.tcc.repository;

import com.projeto.tcc.domain.user.User;
import com.projeto.tcc.domain.user.UserRoles;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

// NOME CORRIGIDO PARA O PADRÃO
public interface UseRepository extends JpaRepository<User, String> {
    
    // RETORNO CORRIGIDO PARA UserDetails
	 Optional<User> findByEmail(String email);
    
    List<User> findByRole(UserRoles role); 
}