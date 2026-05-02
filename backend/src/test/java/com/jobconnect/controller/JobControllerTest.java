package com.jobconnect.controller;

import com.jobconnect.dto.JobRequest;
import com.jobconnect.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JobControllerTest {

    @Mock
    private JobService jobService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JobController jobController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create job successfully")
    void createJob_shouldReturnCreated() {
        JobRequest request = new JobRequest(
                "Software Engineer",
                "Full stack role",
                "Gurugram",
                BigDecimal.valueOf(80000),
                LocalDate.of(2026, 6, 1)
        );

        when(authentication.getName()).thenReturn("employer@test.com");

        ResponseEntity<?> response = jobController.createJob(request, authentication);

        verify(jobService).createJob(request, "employer@test.com");
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Should delete job successfully")
    void deleteJob_shouldReturnNoContent() {
        Long jobId = 1L;

        when(authentication.getName()).thenReturn("employer@test.com");
        ResponseEntity<?> response = jobController.deleteJob(jobId, authentication);

        verify(jobService).deleteJob(jobId);
        assertEquals(200, response.getStatusCode().value());
    }
}