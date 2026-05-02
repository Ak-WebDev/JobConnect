package com.jobconnect.service;

import com.jobconnect.dto.ApplicationResponse;
import com.jobconnect.dto.ApplicationStatusUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse applyToJob(Long jobId, String coverLetter, MultipartFile resume, String seekerEmail);
    List<ApplicationResponse> getMyApplications(String seekerEmail);
    List<ApplicationResponse> getApplicationsForEmployer(String employerEmail);
    ApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatusUpdateRequest request, String employerEmail);
    void deleteApplicationsByJobId(Long jobId);
}