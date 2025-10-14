package com.projeto.tcc.controllers;

import com.projeto.tcc.domain.progress.CourseProgress;
import com.projeto.tcc.domain.progress.ProgressStatus;
import com.projeto.tcc.domain.user.User;
import com.projeto.tcc.dto.SaveProgressRequestDTO;
import com.projeto.tcc.repository.CourseProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final CourseProgressRepository progressRepository;

    @Autowired
    public ProgressController(CourseProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    // Endpoint para SALVAR ou ATUALIZAR o progresso
    @PostMapping
    public ResponseEntity<?> saveOrUpdateProgress(@RequestBody SaveProgressRequestDTO progressDTO) {
        // Pega o usuário que está logado
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Procura se já existe um registro de progresso para este usuário e curso
        Optional<CourseProgress> existingProgressOpt = progressRepository.findByUserAndCourseName(currentUser, progressDTO.courseName());

        CourseProgress progressToSave;
        if (existingProgressOpt.isPresent()) {
            // Se já existe, ATUALIZA o status
            progressToSave = existingProgressOpt.get();
            progressToSave.setStatus(ProgressStatus.valueOf(progressDTO.status().toUpperCase()));
            progressToSave.setLastUpdated(LocalDateTime.now());
        } else {
            // Se não existe, CRIA um novo registro
            progressToSave = new CourseProgress(
                currentUser,
                progressDTO.courseName(),
                ProgressStatus.valueOf(progressDTO.status().toUpperCase())
            );
        }

        progressRepository.save(progressToSave);
        return ResponseEntity.ok("Progresso salvo com sucesso!");
    }

    // Endpoint para BUSCAR todo o progresso de um usuário
    @GetMapping("/me")
    public ResponseEntity<List<CourseProgress>> getUserProgress() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CourseProgress> userProgress = progressRepository.findByUser(currentUser);
        return ResponseEntity.ok(userProgress);
    }
}