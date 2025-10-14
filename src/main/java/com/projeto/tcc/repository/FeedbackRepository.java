package com.projeto.tcc.repository;

import com.projeto.tcc.domain.feedback.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByAlunoIdOrderByDataEnvioDesc(String alunoId);
}