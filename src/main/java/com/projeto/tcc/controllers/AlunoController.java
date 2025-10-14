package com.projeto.tcc.controllers;


import com.projeto.tcc.domain.avaliacao.Avaliacao;
import com.projeto.tcc.domain.user.User;
import com.projeto.tcc.dto.AlunoDashboardDTO;
import com.projeto.tcc.dto.AvaliacaoRequestDTO;
import com.projeto.tcc.dto.DesempenhoDTO;
import com.projeto.tcc.repository.AvaliacaoRepository;
import com.projeto.tcc.repository.UseRepository;
import com.projeto.tcc.security.DesempenhoService;
import com.projeto.tcc.security.StreakService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aluno")
public class AlunoController {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    // --- DEPENDÊNCIAS ADICIONADAS ---
    @Autowired
    private UseRepository userRepository;

    @Autowired
    private StreakService streakService;
    
    @Autowired
    private DesempenhoService desempenhoService;

    /**
     * Busca os dados para o dashboard do aluno, incluindo a contagem de ofensivas.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<AlunoDashboardDTO> getDashboardInfo() {
        User aluno = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // É uma boa prática re-verificar a ofensiva ao carregar o dashboard
        // para "apagar a chama" caso o aluno tenha ficado inativo.
        streakService.updateUserStreak(aluno);
        userRepository.save(aluno);

        AlunoDashboardDTO dto = new AlunoDashboardDTO(aluno.getName(), aluno.getStreakCount());
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/desempenho")
    public ResponseEntity<DesempenhoDTO> getDesempenho() {
        User aluno = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DesempenhoDTO desempenho = desempenhoService.getDesempenhoDoAluno(aluno);
        return ResponseEntity.ok(desempenho);
    }

    /**
     * Salva a nota de uma avaliação (prova/quiz) e atualiza a ofensiva do aluno.
     */
    @PostMapping("/avaliacoes")
    public ResponseEntity<Void> salvarAvaliacao(@RequestBody AvaliacaoRequestDTO dto) {
        User aluno = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setAluno(aluno);
        novaAvaliacao.setNota(dto.nota());
        novaAvaliacao.setCursoId(dto.cursoId());

        avaliacaoRepository.save(novaAvaliacao);

        // --- LÓGICA DE OFENSIVA ADICIONADA ---
        // 1. Calcula a nova contagem de ofensiva
        streakService.updateUserStreak(aluno);
        // 2. Salva o usuário com os dados de ofensiva atualizados
        userRepository.save(aluno);

        return ResponseEntity.ok().build();
    }
}