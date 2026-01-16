package com.jobportal.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.model.Job;
import com.jobportal.model.JobApplication;
import com.jobportal.service.JobService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // ---------------- PUBLIC ENDPOINTS ----------------

    // 11. GET /api/jobs (List all jobs)
    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    // 12. GET /api/jobs/{id} (View job details)
    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    // 6. GET /api/jobs/search (Search jobs)
    @GetMapping("/jobs/search")
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam String skill,
            @RequestParam String location) {
        return ResponseEntity.ok(jobService.searchJobs(skill, location));
    }

    // ---------------- RECRUITER ENDPOINTS ----------------

    // 8. POST /api/jobs (Post a new job - Secured)
    @PostMapping("/jobs")
    public ResponseEntity<Job> postJob(@RequestBody Job job, Principal principal) {
        return ResponseEntity.ok(jobService.postJob(job, principal.getName()));
    }

    // 9. GET /api/jobs/mine (View jobs posted by recruiter)
    @GetMapping("/jobs/mine")
    public ResponseEntity<List<Job>> getMyJobs(Principal principal) {
        return ResponseEntity.ok(jobService.getJobsByRecruiter(principal.getName()));
    }

    // 10. GET /api/applications/{jobId} (View applicants for a specific job)
    @GetMapping("/applications/{jobId}")
    public ResponseEntity<List<JobApplication>> getJobApplications(@PathVariable Long jobId, Principal principal) {
        return ResponseEntity.ok(jobService.getApplicationsForJob(jobId, principal.getName()));
    }

    // ---------------- CANDIDATE ENDPOINTS ----------------

    // 7. POST /api/jobs/{id}/apply (Apply to a job)
    @PostMapping("/jobs/{id}/apply")
    public ResponseEntity<String> applyForJob(@PathVariable Long id, Principal principal) {
        jobService.applyForJob(id, principal.getName());
        return ResponseEntity.ok("Applied successfully");
    }
}