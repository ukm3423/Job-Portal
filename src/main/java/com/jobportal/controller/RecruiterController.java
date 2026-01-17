package com.jobportal.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.dto.JobApplicationResponse;
import com.jobportal.dto.JobCreateRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.model.Job;
import com.jobportal.model.JobApplication;
import com.jobportal.service.RecruiterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterController {

    private final RecruiterService recruiterJobService;

    @PostMapping
    public ResponseEntity<JobResponse> postJob(
            @RequestBody JobCreateRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(
                recruiterJobService.postJob(request, principal.getName())
        );
    }

    @GetMapping("/mine")
    public ResponseEntity<List<JobResponse>> getMyJobs(
            Principal principal
    ) {
        return ResponseEntity.ok(
                recruiterJobService.getJobsByRecruiter(principal.getName())
        );
    }

    @GetMapping("/applications/{jobId}")
    public ResponseEntity<List<JobApplicationResponse>> getApplications(
            @PathVariable Long jobId,
            Principal principal
    ) {
        return ResponseEntity.ok(
                recruiterJobService.getApplicationsForJob(jobId, principal.getName())
        );
    }
}