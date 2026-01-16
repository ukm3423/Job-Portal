package com.jobportal.auth.services;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobportal.auth.dto.CandidateRegisterRequest;
import com.jobportal.auth.dto.JwtRequest;
import com.jobportal.auth.dto.JwtResponse;
import com.jobportal.auth.dto.RecruiterRegisterRequest;
import com.jobportal.auth.models.Role;
import com.jobportal.auth.models.Token;
import com.jobportal.auth.models.TokenType;
import com.jobportal.auth.models.User;
import com.jobportal.auth.repository.TokenRepository;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.auth.security.JwtTokenProvider;
import com.jobportal.common.dto.ErrorCode;
import com.jobportal.common.exceptions.BusinessException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

        private final UserRepository userRepo;
        private final PasswordEncoder passwordEncoder;
        private final JwtTokenProvider jwtProvider;
        private final TokenRepository tokenRepo;
        private final AuthenticationManager authenticationManager;

        // ================= REGISTER CANDIDATE =================
        public void registerCandidate(CandidateRegisterRequest req) {

                if (userRepo.existsByEmail(req.getEmail())) {
                        throw new BusinessException(
                                        ErrorCode.EMAIL_ALREADY_EXISTS,
                                        "Email already registered");
                }

                User user = User.builder()
                                .fullname(req.getName())
                                .email(req.getEmail())
                                .password(passwordEncoder.encode(req.getPassword()))
                                .experience(req.getExperience())
                                .skills(req.getSkills())
                                .location(req.getLocation())
                                .role(Role.CANDIDATE)
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .build();

                userRepo.save(user);
        }

        // ================= REGISTER RECRUITER =================
        public void registerRecruiter(RecruiterRegisterRequest req) {

                if (userRepo.existsByEmail(req.getEmail())) {
                        throw new BusinessException(
                                        ErrorCode.EMAIL_ALREADY_EXISTS,
                                        "Email already registered");
                }

                User user = User.builder()
                                .fullname(req.getName())
                                .company(req.getCompany())
                                .email(req.getEmail())
                                .password(passwordEncoder.encode(req.getPassword()))
                                .role(Role.RECRUITER)
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .build();

                userRepo.save(user);
        }

        // ================= LOGIN =================
        public JwtResponse login(JwtRequest request) {

                try {
                        authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getEmail(),
                                                        request.getPassword()));
                } catch (Exception ex) {
                        throw new BusinessException(
                                        ErrorCode.INVALID_CREDENTIALS,
                                        "Invalid email or password");
                }

                User user = userRepo.findByEmail(request.getEmail())
                                .orElseThrow(() -> new BusinessException(
                                                ErrorCode.USER_NOT_REGISTERED,
                                                "User is not registered"));

                String token = jwtProvider.generateToken(user);

                revokeAllUserTokens(user);
                saveUserToken(user, token);

                return new JwtResponse(token);
        }

        // ================= TOKEN VALIDATION =================
        public boolean isTokenValid(String authHeader) {

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        return false;
                }

                String jwt = authHeader.substring(7);

                if (!jwtProvider.validateToken(jwt)) {
                        revokeToken(jwt);
                        return false;
                }

                return tokenRepo.findByToken(jwt)
                                .filter(t -> !t.getExpired() && !t.getRevoked())
                                .isPresent();
        }

        // ================= TOKEN HELPERS =================
        private void saveUserToken(User user, String jwt) {

                Token token = Token.builder()
                                .token(jwt)
                                .tokenType(TokenType.BEARER)
                                .expired(false)
                                .revoked(false)
                                .user(user)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                tokenRepo.save(token);
        }

        private void revokeAllUserTokens(User user) {

                var tokens = tokenRepo.findAllValidTokensByUser(user.getId());
                if (tokens.isEmpty())
                        return;

                tokens.forEach(t -> {
                        t.setExpired(true);
                        t.setRevoked(true);
                });

                tokenRepo.saveAll(tokens);
        }

        private void revokeToken(String jwt) {
                tokenRepo.findByToken(jwt).ifPresent(t -> {
                        t.setExpired(true);
                        t.setRevoked(true);
                        tokenRepo.save(t);
                });
        }
}
