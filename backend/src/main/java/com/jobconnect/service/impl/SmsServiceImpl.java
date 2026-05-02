package com.jobconnect.service.impl;

import com.jobconnect.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @Value("${twilio.sms.enabled:false}")
    private boolean smsEnabled;

    @PostConstruct
    public void init() {
        if (smsEnabled) {
            Twilio.init(accountSid, authToken);
        }
    }

    @Override
    public void sendSms(String toPhoneNumber, String message) {
        if (!smsEnabled) {
            System.out.println("SMS disabled. Message to " + toPhoneNumber + ": " + message);
            return;
        }

        Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(fromPhoneNumber),
                message
        ).create();
    }

    @Override
    public void sendRegistrationConfirmation(String phoneNumber, String fullName) {
        sendSms(phoneNumber,"Welcome "+ fullName + "! JobConnect account created.");
    }

    @Override
    public void sendApplicationUpdate(String phoneNumber, String jobTitle, String status) {
        sendSms(phoneNumber,"JobConnect: Your application for "+jobTitle+"is now "+status+ ".");
    }

    @Override
    public void sendJobPostedAlerts(String phoneNumber, String jobTitle) {
        sendSms(phoneNumber, "JobConnect: Your job "+jobTitle+"is now active.");
    }
}