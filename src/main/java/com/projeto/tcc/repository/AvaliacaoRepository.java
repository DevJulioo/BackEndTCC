package com.projeto.tcc.repository;

import com.projeto.tcc.domain.avaliacao.Avaliacao;
import com.projeto.tcc.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    // Este método você já tem e é usado pelo Mentor
    Optional<Avaliacao> findTopByAlunoIdOrderByDataRealizacaoDesc(String alunoId);
    
    // --- MÉTODO ADICIONADO ---
    // Este busca TODAS as avaliações de um aluno, necessário para os cálculos
    List<Avaliacao> findAllByAluno(User aluno);
}