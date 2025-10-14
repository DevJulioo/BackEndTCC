package com.projeto.tcc.security;

import com.projeto.tcc.repository.UseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service; // Use @Service para serviços

@Service // Use @Service em vez de @Component para clareza
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UseRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // --- CORREÇÃO AQUI ---
        // Busca o Optional<User> e usa orElseThrow para obter o User ou lançar a exceção.
        return this.repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));
    }
}