package com.jobportal.model;

import com.jobportal.auth.models.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private String requiredSkills; // Comma separated or @ElementCollection
    private Double experienceRequired;
    private String location;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}