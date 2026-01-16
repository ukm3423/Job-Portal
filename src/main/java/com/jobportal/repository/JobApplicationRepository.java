package com.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.auth.models.User;
import com.jobportal.model.Job;
import com.jobportal.model.JobApplication;
@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    // To check if candidate already applied
    boolean existsByJobAndUser(Job job, User user);
    
    // For: GET /api/applications/{jobId}
    List<JobApplication> findByJob(Job job);
}
