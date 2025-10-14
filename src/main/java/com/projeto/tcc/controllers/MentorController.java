package com.projeto.tcc.controllers;

import com.projeto.tcc.domain.avaliacao.Avaliacao;
import com.projeto.tcc.domain.feedback.Feedback;
import com.projeto.tcc.domain.user.User;
import com.projeto.tcc.dto.AlunoDTO;
import com.projeto.tcc.dto.FeedbackRequestDTO;
import com.projeto.tcc.repository.AvaliacaoRepository;
import com.projeto.tcc.repository.FeedbackRepository;
import com.projeto.tcc.repository.UseRepository; // Seu repositório
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mentor")
public class MentorController {

    @Autowired
    private UseRepository useRepository; // CORRIGIDO para usar o nome do seu repositório

    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getMentorDashboardData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = useRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Map<String, Object> dashboardData = Map.of("totalAlunos", 15, "feedbacksPendentes", 4);
        Map<String, Object> response = Map.of("user", currentUser, "dashboard", dashboardData);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/alunos")
    public ResponseEntity<List<AlunoDTO>> getAlunos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User mentor = useRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Mentor não encontrado: " + authentication.getName()));
        
        Set<User> alunosDoMentor = mentor.getAlunos();

        List<AlunoDTO> dtos = alunosDoMentor.stream().map(aluno -> {
            Optional<Avaliacao> ultimaAvaliacao = avaliacaoRepository.findTopByAlunoIdOrderByDataRealizacaoDesc(aluno.getId());
            Double nota = ultimaAvaliacao.map(Avaliacao::getNota).orElse(0.0);
            return new AlunoDTO(aluno.getId(), aluno.getName(), nota);
        })
        .sorted((a, b) -> Double.compare(b.score(), a.score()))
        .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
    /**
     * Endpoint para o mentor enviar um feedback (sem nota).
     */
    @PostMapping("/feedbacks")
    public ResponseEntity<Void> enviarFeedback(@RequestBody FeedbackRequestDTO dto) {
        User mentor = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User aluno = useRepository.findById(dto.alunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Feedback novoFeedback = new Feedback();
        novoFeedback.setMentor(mentor);
        novoFeedback.setAluno(aluno);
        novoFeedback.setTexto(dto.texto());
        
        feedbackRepository.save(novoFeedback);
        return ResponseEntity.ok().build();
    }
}