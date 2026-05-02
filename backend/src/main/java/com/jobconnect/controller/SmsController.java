package com.jobconnect.controller;

import com.jobconnect.dto.SmsRequest;
import com.jobconnect.service.SmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/send")
    public ResponseEntity<String> sendSms(@Valid @RequestBody SmsRequest request) {
        smsService.sendSms(request.getToPhoneNumber(), request.getMessage());
        return ResponseEntity.ok("SMS request processed successfully");
    }
}