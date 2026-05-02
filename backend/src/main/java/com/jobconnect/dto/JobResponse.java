package com.jobconnect.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private BigDecimal salary;
    private LocalDate deadline;
    private LocalDateTime createdAt;
    private Long employerId;
    private String employerName;
    private String employerEmail;
}