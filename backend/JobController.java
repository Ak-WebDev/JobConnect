package com.jobconnect.controller;

import com.jobconnect.dto.JobRequest;
import com.jobconnect.dto.JobResponse;
import com.jobconnect.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.math.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class JobController {

    private final JobService jobService;


    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest request,
                                                 Authentication authentication) {

        return ResponseEntity.ok(jobService.createJob(request, authentication.getName()));
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long jobId,
                                                 @Valid @RequestBody JobRequest request,
                                                 Authentication authentication) {
        return ResponseEntity.ok(jobService.updateJob(jobId, request, authentication.getName()));
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @DeleteMapping("/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable Long jobId,
                                            Authentication authentication) {
        jobService.deleteJob(jobId);
        return ResponseEntity.ok("Job deleted successfully");
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobService.getJobById(jobId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobResponse>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(jobService.searchJobs(keyword, location));
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/my-jobs")
    public ResponseEntity<List<JobResponse>> getEmployerJobs(Authentication authentication) {
        return ResponseEntity.ok(jobService.getEmployerJobs(authentication.getName()));
    }
}