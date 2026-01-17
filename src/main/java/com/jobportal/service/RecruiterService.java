package com.jobportal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jobportal.auth.models.User;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.common.dto.ErrorCode;
import com.jobportal.common.exceptions.BusinessException;
import com.jobportal.dto.JobApplicationResponse;
import com.jobportal.dto.JobCreateRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.mapper.JobApplicationMapper;
import com.jobportal.mapper.JobMapper;
import com.jobportal.model.Job;
import com.jobportal.model.JobApplication;
import com.jobportal.model.Recruiter;
import com.jobportal.repository.JobApplicationRepository;
import com.jobportal.repository.JobRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruiterService {

    private final JobRepository jobRepo;
    private final JobApplicationRepository applicationRepo;
    private final UserRepository userRepo;

    // --- Recruiter Methods ---

    // Requirement 8: Post a new job
    @Transactional
    public JobResponse postJob(JobCreateRequest request, String email) {

        User recruiter = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.RECRUITER_NOT_FOUND,
                                "Recruiter not found"
                        ));

        Job job = JobMapper.toEntity(request, recruiter);
        jobRepo.save(job);

        return JobMapper.toResponse(job);
    }

    // Requirement 9: View jobs posted by recruiter
    public List<JobResponse> getJobsByRecruiter(String email) {
        User recruiter = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.RECRUITER_NOT_FOUND,
                                "Recruiter not found"
                        ));

        return jobRepo.findByUser(recruiter)
                .stream()
                .map(JobMapper::toResponse)
                .toList();
    }

    // Requirement 10: View applicants for a specific job
    public List<JobApplicationResponse> getApplicationsForJob(
            Long jobId,
            String recruiterEmail
    ) {

        Job job = jobRepo.findById(jobId)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.JOB_NOT_FOUND,
                                "Job not found"
                        ));

        if (!job.getUser().getEmail().equals(recruiterEmail)) {
            throw new BusinessException(
                    ErrorCode.UNAUTHORIZED_ACCESS,
                    "You are not allowed to view applications for this job"
            );
        }

        return applicationRepo.findByJob(job)
                .stream()
                .map(JobApplicationMapper::toResponse)
                .toList();
    }


    // --- Candidate Methods ---

    // Requirement 7: Apply to a job
    @Transactional
    public void applyForJob(Long jobId, String candidateEmail) {
    	Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
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