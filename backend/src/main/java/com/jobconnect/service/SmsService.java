package com.jobconnect.service;

public interface SmsService {
    void sendSms(String toPhoneNumber, String message);
    void sendRegistrationConfirmation(String phoneNumber, String fullName);
    void sendApplicationUpdate(String phoneNumber, String jobTitle, String status);
    void sendJobPostedAlerts(String phoneNumber, String jobTitle);
}