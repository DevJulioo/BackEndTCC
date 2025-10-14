package com.projeto.tcc.security;

import com.projeto.tcc.domain.avaliacao.Avaliacao;
import com.projeto.tcc.domain.user.User;
import com.projeto.tcc.dto.DesempenhoDTO;
import com.projeto.tcc.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DesempenhoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public DesempenhoDTO getDesempenhoDoAluno(User aluno) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findAllByAluno(aluno);

        // Calcula a média geral das notas
        double mediaGeral = avaliacoes.stream()
                .mapToDouble(Avaliacao::getNota)
                .average()
                .orElse(0.0);

        // Conta as atividades concluídas
        int atividadesConcluidas = avaliacoes.size();

        // Define o total de atividades (você pode tornar isso dinâmico no futuro)
        int totalAtividades = 16; 

        return new DesempenhoDTO(mediaGeral, atividadesConcluidas, totalAtividades);
    }
}