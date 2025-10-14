package com.projeto.tcc.controllers;

import com.projeto.tcc.domain.user.User;
import com.projeto.tcc.dto.UserProfileDTO;
import com.projeto.tcc.dto.SetLearningGoalRequestDTO;
import com.projeto.tcc.dto.UpdateGoalRequest;
import com.projeto.tcc.dto.UserUpdateRequestDTO;
import com.projeto.tcc.repository.UseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UseRepository userRepository; // NOME CORRIGIDO
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UseRepository userRepository, PasswordEncoder passwordEncoder) { // NOME CORRIGIDO
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Busca os dados do perfil do usuário atualmente logado.
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserProfileDTO userProfile = new UserProfileDTO(currentUser.getName(), currentUser.getEmail(), currentUser.getRole(), currentUser.getAvatarUrl());
        return ResponseEntity.ok(userProfile);
    }

    /**
     * Atualiza as informações do perfil (nome, e-mail, senha).
     */
    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestBody UserUpdateRequestDTO updateRequest) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userToUpdate = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no banco."));

        // Atualiza o nome se foi fornecido
        if (updateRequest.name() != null && !updateRequest.name().isBlank()) {
            userToUpdate.setName(updateRequest.name());
        }

        // Atualiza o e-mail com checagem de duplicidade
        if (updateRequest.email() != null && !updateRequest.email().isBlank() && !updateRequest.email().equalsIgnoreCase(userToUpdate.getEmail())) {
            if (userRepository.findByEmail(updateRequest.email()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Este e-mail já está em uso por outra conta.");
            }
            userToUpdate.setEmail(updateRequest.email());
        }
        
        // Atualiza a senha se foi fornecida
        if (updateRequest.password() != null && !updateRequest.password().isBlank()) {
            userToUpdate.setPassword(passwordEncoder.encode(updateRequest.password()));
        }

        userRepository.save(userToUpdate);
        return ResponseEntity.ok().body("Perfil atualizado com sucesso!");
    }
    
    /**
     * Faz o upload da imagem de avatar do usuário.
     */
    @PostMapping("/me/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userToUpdate = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no banco."));

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Por favor, selecione um arquivo.");
        }

        try {
            String uploadDir = "uploads/avatars/";
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath);

            String fileUrl = "/" + uploadDir + fileName;
            userToUpdate.setAvatarUrl(fileUrl);
            userRepository.save(userToUpdate);

            // AJUSTE: Retorna um JSON com a nova URL, útil para o front-end
            return ResponseEntity.ok().body(Map.of("avatarUrl", fileUrl));
            
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Falha ao carregar o avatar.");
        }
        
        
        
    }
    
    @PutMapping("/me/goal")
    public ResponseEntity<Void> updateUserGoal(@RequestBody UpdateGoalRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        currentUser.setDailyLearningGoalMinutes(request.goal());
        userRepository.save(currentUser);
        return ResponseEntity.ok().build();
    }
    }