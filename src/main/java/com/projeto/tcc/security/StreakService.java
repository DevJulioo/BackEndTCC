package com.projeto.tcc.security;

import com.projeto.tcc.domain.user.User;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class StreakService {

    public void updateUserStreak(User user) {
        LocalDate today = LocalDate.now();
        LocalDate lastActivity = user.getLastActivityDate();

        if (lastActivity == null) {
            // Primeira atividade do usuário
            user.setStreakCount(1);
        } else {
            long daysBetween = ChronoUnit.DAYS.between(lastActivity, today);
            
            if (daysBetween == 1) {
                // Atividade no dia seguinte, continua a ofensiva
                user.setStreakCount(user.getStreakCount() + 1);
            } else if (daysBetween > 1) {
                // Quebrou a ofensiva, reinicia para 1
                user.setStreakCount(1);
            }
            // Se daysBetween for 0, não faz nada, já pontuou hoje.
        }
        
        user.setLastActivityDate(today);
    }
}