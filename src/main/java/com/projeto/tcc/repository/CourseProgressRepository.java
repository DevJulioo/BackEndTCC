package com.projeto.tcc.repository;

import com.projeto.tcc.domain.progress.CourseProgress;
import com.projeto.tcc.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseProgressRepository extends JpaRepository<CourseProgress, Long> {
    // Encontra um progresso específico para um usuário e um curso
    Optional<CourseProgress> findByUserAndCourseName(User user, String courseName);

    // Encontra todo o progresso de um usuário
    List<CourseProgress> findByUser(User user);
}