package com.jobconnect.service;

import com.jobconnect.dto.JobRequest;
import com.jobconnect.entity.Job;
import com.jobconnect.entity.User;
import com.jobconnect.repository.ApplicationRepository;
import com.jobconnect.repository.JobRepository;
import com.jobconnect.repository.UserRepository;
import com.jobconnect.service.impl.JobServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JobServiceImpl jobService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create job and save to repository")
    void createJob_shouldSaveJob() {
        // Arrange
        JobRequest request = new JobRequest(
                "Software Engineer",
                "Full stack role",
                "Gurugram",
                BigDecimal.valueOf(80000),
                LocalDate.of(2026, 6, 1)
        );
        User employer = new User();
        employer.setEmail("employer@test.com");

        Job savedJob = new Job();
        savedJob.setId(1L);
        savedJob.setTitle("Software Engineer");
        savedJob.setDescription("Full stack role");
        savedJob.setLocation("Gurugram");
        savedJob.setSalary(BigDecimal.valueOf(80000));
        savedJob.setDeadline(LocalDate.of(2026, 6, 1));
        savedJob.setEmployer(employer);

        when(userRepository.findByEmail("employer@test.com")).thenReturn(Optional.of(employer));
        when(jobRepository.save(any())).thenReturn(savedJob);
        jobService.createJob(request, "employer@test.com");

        // Assert
        verify(jobRepository).save(any(Job.class));
    }

    @Test
    @DisplayName("Should delete job by ID")
    void deleteJob_shouldDeleteJob() {
        // Arrange
        Long jobId = 1L;

        // Act
        jobService.deleteJob(jobId);

        // Assert
        verify(jobRepository).deleteById(jobId);
        verify(applicationRepository).deleteByJobId(jobId);
    }

}