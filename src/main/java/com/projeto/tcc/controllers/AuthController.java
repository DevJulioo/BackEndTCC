package com.projeto.tcc.controllers;

import com.projeto.tcc.domain.user.User;

import com.projeto.tcc.domain.user.UserRoles;
import com.projeto.tcc.dto.LoginRequestDTO;
import com.projeto.tcc.dto.RegisterRequestDTO;
import com.projeto.tcc.dto.ResponseDTO;
import com.projeto.tcc.repository.UseRepository;

import com.projeto.tcc.security.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UseRepository repository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Value("${mentor.registration.code}")
    private String mentorSecretCode;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UseRepository repository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.repository = repository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated LoginRequestDTO body) { // @Valid agora funciona
        var usernamePassword = new UsernamePasswordAuthenticationToken(body.email(), body.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        User user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user);
        return ResponseEntity.ok(new ResponseDTO(user.getName(), token, user.getRole()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated RegisterRequestDTO body) { // @Valid agora funciona
        
        // O .isPresent() agora vai funcionar porque o UserRepository foi corrigido.
        if (this.repository.findByEmail(body.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este e-mail já está em uso.");
        }
        
        UserRoles role = (body.mentorCode() != null && mentorSecretCode.equals(body.mentorCode().trim()))
                ? UserRoles.MENTOR
                : UserRoles.ALUNO;
        
        String encryptedPassword = passwordEncoder.encode(body.password());
        User newUser = new User(body.name(), body.email(), encryptedPassword, role);

        if (body.dailyLearningGoalMinutes() != null) {
            newUser.setDailyLearningGoalMinutes(body.dailyLearningGoalMinutes());
        }

        this.repository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}