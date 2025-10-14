package com.projeto.tcc.controllers;

import com.projeto.tcc.domain.feedback.Feedback;
import com.projeto.tcc.domain.user.User;
import com.projeto.tcc.dto.FeedbackRequestDTO;
import com.projeto.tcc.dto.FeedbackResponseDTO;
import com.projeto.tcc.repository.FeedbackRepository;
import com.projeto.tcc.repository.UseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private UseRepository userRepository;

    // --- Endpoints para o ALUNO ---
    @GetMapping
    public ResponseEntity<List<FeedbackResponseDTO>> getMeusFeedbacks() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<FeedbackResponseDTO> dtos = feedbackRepository.findByAlunoIdOrderByDataEnvioDesc(currentUser.getId())
            .stream().map(FeedbackResponseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/lido")
    public ResponseEntity<Void> marcarComoLido(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean lido = body.get("lido");
        return feedbackRepository.findById(id).map(feedback -> {
            feedback.setLido(lido);
            feedbackRepository.save(feedback);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/marcar-todos-lidos")
    public ResponseEntity<Void> marcarTodosComoLidos() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Feedback> feedbacksNaoLidos = feedbackRepository.findByAlunoIdOrderByDataEnvioDesc(currentUser.getId())
            .stream().filter(f -> !f.isLido()).toList();
        feedbacksNaoLidos.forEach(f -> f.setLido(true));
        feedbackRepository.saveAll(feedbacksNaoLidos);
        return ResponseEntity.ok().build();
    }
    
    // --- Endpoint para o MENTOR enviar um novo feedback ---
    @PostMapping("/mentor")
    public ResponseEntity<Void> enviarFeedbackPeloMentor(@RequestBody FeedbackRequestDTO dto) {
        User mentor = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User aluno = userRepository.findById(dto.alunoId())
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Feedback novoFeedback = new Feedback();
        novoFeedback.setMentor(mentor);
        novoFeedback.setAluno(aluno);
        novoFeedback.setTexto(dto.texto());
       
        
        feedbackRepository.save(novoFeedback);
        return ResponseEntity.ok().build();
    }
}