package com.jobportal.serviceTests;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jobportal.auth.models.User;
import com.jobportal.dto.JobResponse;
import com.jobportal.model.Job;
import com.jobportal.repository.JobRepository;
import com.jobportal.service.JobQueryService;

@ExtendWith(MockitoExtension.class)
class JobQueryServiceTest {

    @Mock
    private JobRepository jobRepo;

    @InjectMocks
    private JobQueryService jobQueryService;

    @Test
    void getAllJobs_success() {
    	when(jobRepo.findAll()).thenReturn(List.of(mockJob()));

        List<JobResponse> jobs = jobQueryService.getAllJobs();

        assertEquals(1, jobs.size());
        verify(jobRepo).findAll();
    }

    @Test
    void getJobById_success() {
    	when(jobRepo.findById(1L)).thenReturn(Optional.of(mockJob()));


        JobResponse response = jobQueryService.getJobById(1L);

        assertNotNull(response);
    }

    @Test
    void getJobById_notFound() {
        when(jobRepo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> jobQueryService.getJobById(1L));
    }

    @Test
    void searchJobs_success() {
    	when(jobRepo
    		    .findByRequiredSkillsContainingIgnoreCaseAndLocationContainingIgnoreCase(
    		        "Java", "NY"))
    		    .thenReturn(List.of(mockJob()));

        List<JobResponse> jobs =
                jobQueryService.searchJobs("Java", "NY");

        assertFalse(jobs.isEmpty());
    }
    
    private Job mockJob() {
        User recruiter = new User();
        recruiter.setEmail("recruiter@test.com");
        recruiter.setCompany("ABC Corp");

        Job job = new Job();
        job.setId(1L);
        job.setTitle("Java Developer");
        job.setUser(recruiter);

        return job;
    }

}
