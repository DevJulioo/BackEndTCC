package com.projeto.tcc.repository;

import com.projeto.tcc.domain.user.Reuniao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ReuniaoRepository extends JpaRepository<Reuniao, UUID> {
    
    // Assinatura corrigida para aceitar o ID do usuário como String
    List<Reuniao> findAllByAlunoIdOrMentorId(String alunoId, String mentorId);
}