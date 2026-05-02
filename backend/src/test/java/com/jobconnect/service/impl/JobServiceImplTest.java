package com.jobconnect.service.impl;

import com.jobconnect.repository.ApplicationRepository;
import com.jobconnect.repository.JobRepository;
import com.jobconnect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JobServiceImplTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private JobServiceImpl jobServiceImpl;

    @Test
    void deleteJob_shouldDeleteApplicationsFirst_thenDeleteJob() {
        Long jobId = 1L;

        jobServiceImpl.deleteJob(jobId);

        verify(applicationRepository).deleteByJobId(jobId);
        verify(jobRepository).deleteById(jobId);
    }
}