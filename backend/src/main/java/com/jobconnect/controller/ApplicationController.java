package com.jobconnect.controller;

import com.jobconnect.dto.ApplicationResponse;
import com.jobconnect.dto.ApplicationStatusUpdateRequest;
import com.jobconnect.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApplicationResponse> applyToJob(@RequestParam("jobId") Long jobId,
                                                          @RequestParam(required = false) String coverLetter,
                                                          @RequestPart("resume") MultipartFile resume,
                                                          Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(
                applicationService.applyToJob(jobId, coverLetter, resume, email)
        );
    }

    @PreAuthorize("hasRole('JOB_SEEKER')")
    @GetMapping("/my-applications")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.getMyApplications(email));
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForEmployer(@PathVariable Long jobId,
                                                                                Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.getApplicationsForEmployer(email));
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationStatusUpdateRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(
                applicationService.updateApplicationStatus(applicationId, request, email)
        );
    }
}