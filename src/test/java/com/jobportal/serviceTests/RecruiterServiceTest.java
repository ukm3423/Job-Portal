package com.jobportal.serviceTests;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jobportal.auth.models.User;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.common.exceptions.BusinessException;
import com.jobportal.dto.JobCreateRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.model.Job;
import com.jobportal.model.JobApplication;
import com.jobportal.repository.JobApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.service.RecruiterService;

@ExtendWith(MockitoExtension.class)
class RecruiterServiceTest {

    @Mock
    private JobRepository jobRepo;

    @Mock
    private JobApplicationRepository applicationRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private RecruiterService recruiterService;

    @Test
    void postJob_success() {
        User recruiter = new User();
        recruiter.setEmail("recruiter@test.com");

        JobCreateRequest request = new JobCreateRequest();
        request.setTitle("Java Dev");

        when(userRepo.findByEmail("recruiter@test.com"))
                .thenReturn(Optional.of(recruiter));
        when(jobRepo.save(any(Job.class)))
                .thenAnswer(i -> i.getArgument(0));

        JobResponse response =
                recruiterService.postJob(request, "recruiter@test.com");

        assertNotNull(response);
        verify(jobRepo).save(any(Job.class));
    }

    @Test
    void getJobsByRecruiter_success() {
        User recruiter = new User();
        recruiter.setEmail("recruiter@test.com");

        when(userRepo.findByEmail("recruiter@test.com"))
                .thenReturn(Optional.of(recruiter));
        when(jobRepo.findByUser(recruiter)).thenReturn(List.of(mockJobWithRecruiter()));

        List<JobResponse> jobs =
                recruiterService.getJobsByRecruiter("recruiter@test.com");

        assertEquals(1, jobs.size());
    }

    @Test
    void getApplicationsForJob_success() {
        User recruiter = new User();
        recruiter.setEmail("recruiter@test.com");

        Job job = new Job();
        job.setUser(recruiter);

        when(jobRepo.findById(1L))
                .thenReturn(Optional.of(job));
        when(applicationRepo.findByJob(job))
        .thenReturn(List.of(mockJobApplication(job)));


        List<?> apps =
                recruiterService.getApplicationsForJob(1L, "recruiter@test.com");

        assertEquals(1, apps.size());
    }

    @Test
    void getApplicationsForJob_unauthorized() {
        User owner = new User();
        owner.setEmail("owner@test.com");

        Job job = new Job();
        job.setUser(owner);

        when(jobRepo.findById(1L))
                .thenReturn(Optional.of(job));

        assertThrows(BusinessException.class,
                () -> recruiterService.getApplicationsForJob(
                        1L, "other@test.com"));
    }
    
    private Job mockJobWithRecruiter() {
        User recruiter = new User();
        recruiter.setEmail("recruiter@test.com");
        recruiter.setCompany("ABC Corp");

        Job job = new Job();
        job.setId(1L);
        job.setTitle("Java Developer");
        job.setUser(recruiter);

        return job;
    }
    
    private JobApplication mockJobApplication(Job job) {
        User candidate = new User();
        candidate.setId(10L);
        candidate.setEmail("candidate@test.com");
        candidate.setFullname("John");
        candidate.setLocation("NY");
        candidate.setExperience(3D);

        JobApplication application = new JobApplication();
        application.setId(100L);
        application.setJob(job);
        application.setUser(candidate);

        return application;
    }


}
