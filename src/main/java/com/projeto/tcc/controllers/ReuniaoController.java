package com.projeto.tcc.controllers;


import com.projeto.tcc.domain.user.Reuniao;
import com.projeto.tcc.domain.user.User;
import com.projeto.tcc.domain.user.UserRoles;
import com.projeto.tcc.dto.*;
import com.projeto.tcc.repository.ReuniaoRepository;
import com.projeto.tcc.repository.UseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ReuniaoController {

    private final UseRepository userRepository; // Corrigido
    private final ReuniaoRepository reuniaoRepository;

    @Autowired
    public ReuniaoController(UseRepository userRepository, ReuniaoRepository reuniaoRepository) { // Corrigido
        this.userRepository = userRepository;
        this.reuniaoRepository = reuniaoRepository;
    }

    @GetMapping("/api/mentores")
    public ResponseEntity<List<MentorDTO>> getAllMentores() {
        List<MentorDTO> mentores = userRepository.findByRole(UserRoles.MENTOR) // Corrigido
                .stream()
                .map(user -> new MentorDTO(user.getId(), user.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(mentores);
    }

    @GetMapping("/api/reunioes")
    public ResponseEntity<List<ReuniaoResponseDTO>> getMinhasReunioes() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ReuniaoResponseDTO> reunioes = reuniaoRepository.findAllByAlunoIdOrMentorId(currentUser.getId(), currentUser.getId())
                .stream()
                .map(ReuniaoResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reunioes);
    }

    @PostMapping("/api/reunioes")
    public ResponseEntity<Void> criarReuniao(@RequestBody ReuniaoRequestDTO dados) {
        User aluno = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User mentor = userRepository.findById(dados.mentorId())
                .orElseThrow(() -> new RuntimeException("Mentor não encontrado"));

        Reuniao novaReuniao = new Reuniao();
        novaReuniao.setAluno(aluno);
        novaReuniao.setMentor(mentor);
        novaReuniao.setAssunto(dados.assunto());
        novaReuniao.setObservacoes(dados.observacoes());
        novaReuniao.setModalidade(dados.modalidade());
        novaReuniao.setData(dados.data());
        novaReuniao.setHora(dados.hora());
        novaReuniao.setStatus("PENDENTE");
        reuniaoRepository.save(novaReuniao);

        // --- LÓGICA DE ASSOCIAÇÃO ADICIONADA ---
        // Adiciona o aluno à lista de alunos do mentor
        mentor.getAlunos().add(aluno);
        userRepository.save(mentor); // Salva o mentor com a lista de alunos atualizada

        return ResponseEntity.created(null).build();
    }

    @PatchMapping("/api/reunioes/{reuniaoId}/status")
    public ResponseEntity<?> updateReuniaoStatus(
            @PathVariable UUID reuniaoId,
            @RequestBody StatusUpdateRequestDTO request) {
        
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Reuniao reuniao = reuniaoRepository.findById(reuniaoId)
                .orElseThrow(() -> new RuntimeException("Reunião não encontrada."));

        if (!reuniao.getMentor().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado.");
        }

        reuniao.setStatus(request.status());
        reuniaoRepository.save(reuniao);
        return ResponseEntity.ok().build();
    }
}