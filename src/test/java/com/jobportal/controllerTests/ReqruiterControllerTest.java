package com.jobportal.controllerTests;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.controller.RecruiterController;
import com.jobportal.dto.JobCreateRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.service.RecruiterService;

@WebMvcTest(RecruiterController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecruiterControllerTest extends BaseControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecruiterService recruiterJobService;

    @Autowired
    private ObjectMapper objectMapper;

    private Principal recruiterPrincipal() {
        return () -> "recruiter@test.com";
    }

    @Test
    @WithMockUser(roles = "RECRUITER")
    void postJob_success() throws Exception {

        JobCreateRequest request = new JobCreateRequest();
        request.setTitle("Java Dev");
        request.setDescription("Backend");
        request.setLocation("Bangalore");
        request.setRequiredSkills("Java, Spring");
        request.setExperienceRequired(3.0);

        when(recruiterJobService.postJob(request, "recruiter@test.com"))
                .thenReturn(mockJobResponse());

        mockMvc.perform(post("/api/jobs")
                .principal(recruiterPrincipal())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    private JobResponse mockJobResponse() {
        return new JobResponse(
                1L,
                "Java Dev",
                "Backend",
                "Bangalore",
                "Java, Spring",
                3.0,
                "ABC Corp",
                "recruiter@test.com"
        );
    }
}
