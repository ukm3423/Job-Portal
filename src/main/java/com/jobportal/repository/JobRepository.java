package com.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.auth.models.User;
import com.jobportal.model.Job;
import com.jobportal.model.Recruiter;
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    // For: GET /api/jobs/search?skill=Java&location=Kolkata
    // uses 'Containing' to match partial strings (e.g. "Java" in "Java, Spring")
    List<Job> findByRequiredSkillsContainingIgnoreCaseAndLocationContainingIgnoreCase(String skill, String location);
    
    // For: GET /api/jobs/mine
    List<Job> findByUser(User user);
}
