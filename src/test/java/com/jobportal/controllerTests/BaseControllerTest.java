package com.jobportal.controllerTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jobportal.auth.services.UserDetailsServiceImpl;
import com.jobportal.auth.security.JwtRequestFilter;

@AutoConfigureMockMvc(addFilters = false) // Disables security filters for all extending tests
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    // These mocks satisfy the dependencies that cause the "NoSuchBeanDefinitionException"
    @MockBean
    protected UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    protected JwtRequestFilter jwtRequestFilter;
}