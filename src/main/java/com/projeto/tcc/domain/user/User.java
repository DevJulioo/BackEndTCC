package com.projeto.tcc.domain.user;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRoles role;
    private String avatarUrl;

    // --- RELACIONAMENTO MENTOR-ALUNO (LADO "DONO") ---
    // Um mentor tem uma lista de alunos.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "mentor_alunos",
        joinColumns = @JoinColumn(name = "mentor_id"),
        inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private Set<User> alunos = new HashSet<>();

    // ======================================================
    // ============== CORREÇÃO PRINCIPAL AQUI ===============
    // Adicionamos o "outro lado" da relação.
    // Um aluno tem uma lista de mentores.
    // "mappedBy" indica que esta é a parte inversa da relação já mapeada no campo "alunos".
    // ======================================================
    @ManyToMany(mappedBy = "alunos", fetch = FetchType.EAGER)
    private Set<User> mentores = new HashSet<>();
    

    // --- CAMPOS PARA METAS E OFENSIVAS ---
    @Column(name = "daily_learning_goal_minutes")
    private Integer dailyLearningGoalMinutes;
    @Column(name = "streak_count")
    private Integer streakCount = 0;
    @Column(name = "last_activity_date")
    private LocalDate lastActivityDate;

    // Construtores
    public User() {}
    public User(String name, String email, String password, UserRoles role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters e Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public UserRoles getRole() { return role; }
    public void setRole(UserRoles role) { this.role = role; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public Integer getDailyLearningGoalMinutes() { return dailyLearningGoalMinutes; }
    public void setDailyLearningGoalMinutes(Integer dailyLearningGoalMinutes) { this.dailyLearningGoalMinutes = dailyLearningGoalMinutes; }
    public Integer getStreakCount() { return streakCount; }
    public void setStreakCount(Integer streakCount) { this.streakCount = streakCount; }
    public LocalDate getLastActivityDate() { return lastActivityDate; }
    public void setLastActivityDate(LocalDate lastActivityDate) { this.lastActivityDate = lastActivityDate; }

    public Set<User> getAlunos() {
        return alunos;
    }
    public void setAlunos(Set<User> alunos) {
        this.alunos = alunos;
    }

    // ======================================================
    // ============== GETTER E SETTER ADICIONADOS ===========
    // ======================================================
    public Set<User> getMentores() {
        return mentores;
    }
    public void setMentores(Set<User> mentores) {
        this.mentores = mentores;
    }

    // Métodos do UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRoles.MENTOR) {
            return List.of(new SimpleGrantedAuthority("ROLE_MENTOR"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_ALUNO"));
        }
    }
    @Override public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }
    @Override public String getUsername() { return this.email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
    
}