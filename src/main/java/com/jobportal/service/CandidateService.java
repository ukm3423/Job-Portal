package com.jobportal.service;

import org.springframework.stereotype.Service;

import com.jobportal.auth.models.User;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.common.dto.ErrorCode;
import com.jobportal.common.exceptions.BusinessException;
import com.jobportal.dto.CandidateProfileResponse;
import com.jobportal.model.CandidateDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateService {
    
    private final UserRepository userRepo;

    // Requirement 4: Get profile
    public CandidateProfileResponse getProfile(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.USER_NOT_FOUND,
                                "Candidate Not Found"
                        ));

        return mapToResponse(user);
    }

    // Requirement 5: Update profile
    @Transactional
    public CandidateProfileResponse updateProfile(
            String email,
            CandidateDTO request
    ) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.USER_NOT_FOUND,
                                "Candidate Not Found"
                        ));

        user.setFullname(request.getName());
        user.setExperience(request.getExperience());
        user.setSkills(request.getSkills());
        user.setLocation(request.getLocation());

        userRepo.save(user);

        return mapToResponse(user);
    }

    private CandidateProfileResponse mapToResponse(User user) {
        return new CandidateProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getFullname(),
                user.getExperience(),
                user.getSkills(),
                user.getLocation()
        );
    }
}
