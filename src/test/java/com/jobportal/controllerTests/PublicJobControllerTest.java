package com.jobportal.controllerTests;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jobportal.controller.PublicJobController;
import com.jobportal.dto.JobResponse;
import com.jobportal.service.JobQueryService;

@WebMvcTest(PublicJobController.class)
@AutoConfigureMockMvc(addFilters = false)
class PublicJobControllerTest extends BaseControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobQueryService jobQueryService;

    @Test
    void getAllJobs_success() throws Exception {
        when(jobQueryService.getAllJobs())
                .thenReturn(List.of(mockJobResponse()));

        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk());
    }

    @Test
    void getJobById_success() throws Exception {
        when(jobQueryService.getJobById(1L))
                .thenReturn(mockJobResponse());

        mockMvc.perform(get("/api/jobs/1"))
                .andExpect(status().isOk());
    }

    private JobResponse mockJobResponse() {
        return new JobResponse(
                1L,
                "Java Developer",
                "Backend role",
                "Bangalore",
                "Java, Spring",
                3.0,
                "ABC Corp",
                "recruiter@test.com"
        );
    }
}
