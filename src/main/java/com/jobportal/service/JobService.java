package com.jobportal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jobportal.auth.models.User;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.common.dto.ErrorCode;
import com.jobportal.common.exceptions.BusinessException;
import com.jobportal.model.Job;
import com.jobportal.model.JobApplication;
import com.jobportal.model.Recruiter;
import com.jobportal.repository.JobApplicationRepository;
import com.jobportal.repository.JobRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepo;
    private final JobApplicationRepository applicationRepo;
    private final UserRepository userRepo;

    // --- Public Methods ---

    // Requirement 11: List all jobs
    public List<Job> getAllJobs() {
        return jobRepo.findAll();
    }

    // Requirement 12: View job details
    public Job getJobById(Long id) {
        return jobRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + id));
    }

    // Requirement 6: Search jobs
    public List<Job> searchJobs(String skill, String location) {
        // Assumes you have this method in JobRepository as discussed
        return jobRepo.findByRequiredSkillsContainingIgnoreCaseAndLocationContainingIgnoreCase(skill, location);
    }

    // --- Recruiter Methods ---

    // Requirement 8: Post a new job
    @Transactional
    public Job postJob(Job job, String recruiterEmail) {
        User recruiter = userRepo.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        
        job.setUser(recruiter);
        return jobRepo.save(job);
    }

    // Requirement 9: View jobs posted by recruiter
    public List<Job> getJobsByRecruiter(String recruiterEmail) {
        User recruiter = userRepo.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        
        return jobRepo.findByUser(recruiter);
    }

    // Requirement 10: View applicants for a specific job
    public List<JobApplication> getApplicationsForJob(Long jobId, String recruiterEmail) {
        Job job = getJobById(jobId);

        // Security Check: Ensure the recruiter requesting this OWNS the job
        if (!job.getUser().getEmail().equals(recruiterEmail)) {
            throw new RuntimeException("Unauthorized: You cannot view applications for a job you did not post.");
        }

        return applicationRepo.findByJob(job);
    }

    // --- Candidate Methods ---

    // Requirement 7: Apply to a job
    @Transactional
    public void applyForJob(Long jobId, String candidateEmail) {
        Job job = getJobById(jobId);
        User candidate = userRepo.findByEmail(candidateEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.CANDIDATE_NOT_FOUND, "Candidate not found"));

        // Check if already applied
        if (applicationRepo.existsByJobAndUser(job, candidate)) {
            throw new RuntimeException("You have already applied for this job.");
        }

        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setUser(candidate);
        application.setAppliedAt(LocalDateTime.now());

        applicationRepo.save(application);
    }
}