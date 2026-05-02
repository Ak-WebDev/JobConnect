package com.jobconnect.service.impl;

import com.jobconnect.dto.ApplicationResponse;
import com.jobconnect.dto.ApplicationStatusUpdateRequest;
import com.jobconnect.entity.Application;
import com.jobconnect.entity.ApplicationStatus;
import com.jobconnect.entity.Job;
import com.jobconnect.entity.User;
import com.jobconnect.exception.BadRequestException;
import com.jobconnect.exception.ResourceNotFoundException;
import com.jobconnect.exception.UnauthorizedActionException;
import com.jobconnect.repository.ApplicationRepository;
import com.jobconnect.repository.JobRepository;
import com.jobconnect.repository.UserRepository;
import com.jobconnect.service.ApplicationService;
import com.jobconnect.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    private final SmsService smsService;
    private final String uploadDir = "uploads/resumes/";

    @Override
    public ApplicationResponse applyToJob(Long jobId, String coverLetter, MultipartFile resume, String seekerEmail) {
        User seeker = userRepository.findByEmail(seekerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        applicationRepository.findByJobAndJobSeeker(job, seeker)
                .ifPresent(a -> {
                    throw new BadRequestException("You have already applied for this job");
                });

        if (resume == null || resume.isEmpty()) {
            throw new BadRequestException("Resume file is required");
        }

        String fileName = saveResume(resume);

        Application application = Application.builder()
                .coverLetter(coverLetter)
                .resumeUrl(fileName)
                .status(ApplicationStatus.APPLIED)
                .appliedAt(LocalDateTime.now())
                .job(job)
                .jobSeeker(seeker)
                .build();

        return mapToResponse(applicationRepository.save(application));
    }

    @Override
    public List<ApplicationResponse> getMyApplications(String seekerEmail) {
        User seeker = userRepository.findByEmail(seekerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));

        return applicationRepository.findByJobSeeker(seeker)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ApplicationResponse> getApplicationsForEmployer(String employerEmail) {
        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found"));

        List<Job> jobs = jobRepository.findByEmployer(employer);
        List<ApplicationResponse> responses = new ArrayList<>();

        for (Job job : jobs) {
            List<Application> applications = applicationRepository.findByJob(job);
            responses.addAll(applications.stream().map(this::mapToResponse).toList());
        }

        return responses;
    }

    @Override
    public ApplicationResponse updateApplicationStatus(Long applicationId,
                                                       ApplicationStatusUpdateRequest request,
                                                       String employerEmail) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getJob().getEmployer().getEmail().equals(employerEmail)) {
            throw new UnauthorizedActionException("You can update only applications for your own jobs");
        }

        application.setStatus(request.getStatus());
        try {
            smsService.sendApplicationUpdate(application.getJobSeeker().getPhoneNumber(),
                    application.getJob().getTitle(), application.getStatus().name());
        }catch (Exception e){
            System.out.println("SMS sending failed: "+e.getMessage());
        }
        return mapToResponse(applicationRepository.save(application));
    }

    @Override
    public void deleteApplicationsByJobId(Long jobId) {
        applicationRepository.deleteByJobId(jobId);
    }

    private String saveResume(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + "_" + originalFilename;
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new BadRequestException("Failed to store resume file");
        }
    }

    private ApplicationResponse mapToResponse(Application application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .coverLetter(application.getCoverLetter())
                .resumeUrl(application.getResumeUrl())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())
                .jobId(application.getJob().getId())
                .jobTitle(application.getJob().getTitle())
                .seekerId(application.getJobSeeker().getId())
                .seekerName(application.getJobSeeker().getFullName())
                .seekerEmail(application.getJobSeeker().getEmail())
                .build();
    }
}