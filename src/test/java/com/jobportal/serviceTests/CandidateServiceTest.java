package com.jobportal.serviceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.jobportal.auth.models.User;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.common.exceptions.BusinessException;
import com.jobportal.dto.CandidateProfileResponse;
import com.jobportal.model.CandidateDTO;
import com.jobportal.service.CandidateService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private CandidateService candidateService;

    @Test
    void getProfile_success() {
        User user = new User();
        user.setId(1L);
        user.setEmail("candidate@test.com");
        user.setFullname("John");

        when(userRepo.findByEmail("candidate@test.com"))
                .thenReturn(Optional.of(user));

        CandidateProfileResponse response =
                candidateService.getProfile("candidate@test.com");

        assertEquals("candidate@test.com", response.getEmail());
    }

    @Test
    void getProfile_notFound() {
        when(userRepo.findByEmail("candidate@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> candidateService.getProfile("candidate@test.com"));
    }

    @Test
    void updateProfile_success() {
        User user = new User();
        user.setEmail("candidate@test.com");

        CandidateDTO dto = new CandidateDTO();
        dto.setName("Updated Name");
        dto.setExperience(3.0);
        dto.setSkills("Java");
        dto.setLocation("NY");

        when(userRepo.findByEmail("candidate@test.com"))
                .thenReturn(Optional.of(user));

        CandidateProfileResponse response =
                candidateService.updateProfile("candidate@test.com", dto);

        assertEquals("Updated Name", response.getFullName());
        verify(userRepo).save(user);
    }

    @Test
    void updateProfile_userNotFound() {
        CandidateDTO dto = new CandidateDTO();

        when(userRepo.findByEmail("candidate@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> candidateService.updateProfile("candidate@test.com", dto));
    }
}

