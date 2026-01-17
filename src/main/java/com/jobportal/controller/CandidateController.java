package com.jobportal.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.dto.CandidateProfileResponse;
import com.jobportal.model.CandidateDTO;
import com.jobportal.service.CandidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateController {

	private final CandidateService candidateService;

	// 4. GET /api/candidates/profile (Secured)
	@GetMapping("/candidates/profile")
	public ResponseEntity<CandidateProfileResponse> getProfile(Principal principal) {
		String email = principal.getName(); // contains the email from the JWT token
		return ResponseEntity.ok(candidateService.getProfile(email));
	}

	// 5. PUT /api/candidates/profile (Update profile)
	@PutMapping("/candidates/profile")
	public ResponseEntity<CandidateProfileResponse> updateProfile(@RequestBody CandidateDTO candidate,
			Principal principal) {
		return ResponseEntity.ok(candidateService.updateProfile(principal.getName(), candidate));
	}

	// 7. POST /api/jobs/{id}/apply (Apply to a job)
	@PostMapping("/jobs/{jobId}/apply")
	public ResponseEntity<Void> apply(@PathVariable Long jobId, Principal principal) {
		candidateService.apply(jobId, principal.getName());
		return ResponseEntity.ok().build();
	}
}
