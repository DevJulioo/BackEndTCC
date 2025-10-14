package com.projeto.tcc.domain.progress;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projeto.tcc.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "course_progress")
public class CourseProgress {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id")
	    @JsonIgnore // Evita loops infinitos ao converter para JSON
	    private User user;

	    @Column(name = "course_name")
	    private String courseName;

	    @Enumerated(EnumType.STRING)
	    private ProgressStatus status;

	    @Column(name = "last_updated")
	    private LocalDateTime lastUpdated;
	    
	    // Construtores
	    public CourseProgress() {
	    }

	    public CourseProgress(User user, String courseName, ProgressStatus status) {
	        this.user = user;
	        this.courseName = courseName;
	        this.status = status;
	        this.lastUpdated = LocalDateTime.now();
	    }

	    // Getters e Setters
	    public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }
	    public User getUser() { return user; }
	    public void setUser(User user) { this.user = user; }
	    public String getCourseName() { return courseName; }
	    public void setCourseName(String courseName) { this.courseName = courseName; }
	    public ProgressStatus getStatus() { return status; }
	    public void setStatus(ProgressStatus status) { this.status = status; }
	    public LocalDateTime getLastUpdated() { return lastUpdated; }
	    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
	}
