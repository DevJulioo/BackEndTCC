package com.projeto.tcc.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.projeto.tcc.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            // MANTIDO COMO HMAC256, CONFORME SOLICITADO
            Algorithm algorithm = Algorithm.HMAC256(secret);
            
            return JWT.create()
                    .withIssuer("educasenai-api")
                    .withSubject(user.getEmail())
                    // A ADIÇÃO DA ROLE FOI MANTIDA, POIS É ESSENCIAL
                    .withClaim("role", user.getRole().name()) 
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token de autenticação.", exception);
        }
    }

    public String validateToken(String token) {
        try {
            // MANTIDO COMO HMAC256, CONFORME SOLICITADO
            Algorithm algorithm = Algorithm.HMAC256(secret);
            
            return JWT.require(algorithm)
                    .withIssuer("educasenai-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}