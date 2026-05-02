package com.jobconnect.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsRequest {

    @NotBlank(message = "Recipient phone number is required")
    private String toPhoneNumber;

    @NotBlank(message = "Message is required")
    private String message;
}