package com.jobportal.auth.controllers;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.auth.dto.CandidateRegisterRequest;
import com.jobportal.auth.dto.JwtRequest;
import com.jobportal.auth.dto.JwtResponse;
import com.jobportal.auth.dto.RecruiterRegisterRequest;
import com.jobportal.auth.models.Token;
import com.jobportal.auth.repository.TokenRepository;
import com.jobportal.auth.services.AuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "JWT Authentication")
public class JwtAuthController {

	/**
	 * * ===========================================================================
	 * * ======================== Module : JWTAuthController =======================
	 * * ======================== Created By : Umesh Kumar =========================
	 * * ======================== Created On : 16-01-2026 ==========================
	 * * ===========================================================================
	 * * | Code Status : On
	 */
	private final TokenRepository tokenRepo;
	private final AuthService authService;
	
	@Value("${spring.app.jwtSecret}")
	private String secretKey;

	// ================= RECRUITER CANDIDATE =================
	@PostMapping("/register/candidate")
	public ResponseEntity<?> registerCandidate(@Valid @RequestBody CandidateRegisterRequest req) {
		authService.registerCandidate(req);
		return ResponseEntity.status(HttpStatus.CREATED).body("Candidate registered successfully");
	}

	// ================= RECRUITER REGISTER =================
	@PostMapping("/register/recruiter")
	public ResponseEntity<?> registerRecruiter(@Valid @RequestBody RecruiterRegisterRequest req) {
		authService.registerRecruiter(req);
		return ResponseEntity.status(HttpStatus.CREATED).body("Recruiter registered successfully");
	}

	/**
	 * * User Login
	 * 
	 * @param authenticationRequest
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> createAuthenticationToken(@Valid @RequestBody JwtRequest authenticationRequest) {
		return ResponseEntity.ok(authService.login(authenticationRequest));
	}

	@GetMapping("/check-validity")
	public boolean checkTokenValidity(HttpServletRequest request, HttpServletResponse response) {

		final String authorizationHeader = request.getHeader("Authorization");
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
		}

		if (jwt == null) {
			return false;
		}

		// Parse the token to extract the claims
		Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
		Claims claims;
		try {
			claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
		} catch (Exception e) {
			return false; // Token is invalid
		}

		// Check if the token is expired
		Date expiration = claims.getExpiration();
		if (expiration.before(new Date())) {
			// Mark the token as expired and revoked in the database
			var validUserTokens = tokenRepo.findByToken(jwt);
			if (validUserTokens.isPresent()) {
				Token token = validUserTokens.get();
				token.setExpired(true);
				token.setRevoked(true);
				tokenRepo.save(token);
			}
			return false; // Token is expired
		}

		// Check if the token is valid in the database
		var validUserTokens = tokenRepo.findByToken(jwt);
		if (validUserTokens.isPresent() && !validUserTokens.get().getExpired() && !validUserTokens.get().getRevoked()) {
			return true; // Token is valid
		}

		return false; // Token is invalid or not found in the database
	}

}