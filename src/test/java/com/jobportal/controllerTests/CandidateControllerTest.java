package com.jobportal.controllerTests;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.jobportal.controller.CandidateController;
import com.jobportal.service.CandidateService;

@WebMvcTest(CandidateController.class)
@AutoConfigureMockMvc(addFilters = false)
class CandidateControllerTest extends BaseControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService candidateJobService;

    private Principal candidatePrincipal() {
        return () -> "candidate@test.com";
    }

    @Test
    @WithMockUser(roles = "CANDIDATE")
    void applyForJob_success() throws Exception {

        doNothing().when(candidateJobService)
                .apply(anyLong(), anyString());

        mockMvc.perform(post("/api/jobs/1/apply")
                .principal(candidatePrincipal()))
                .andExpect(status().isOk());
    }

}
