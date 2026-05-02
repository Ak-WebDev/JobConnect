package com.jobconnect.service;

import com.jobconnect.dto.JobRequest;
import com.jobconnect.dto.JobResponse;

import java.util.List;

public interface JobService {
    JobResponse createJob(JobRequest request, String employerEmail);
    JobResponse updateJob(Long jobId, JobRequest request, String employerEmail);
    void deleteJob(Long jobId);
    List<JobResponse> getAllJobs();
    JobResponse getJobById(Long jobId);
    List<JobResponse> searchJobs(String keyword, String location);
    List<JobResponse> getEmployerJobs(String employerEmail);
}