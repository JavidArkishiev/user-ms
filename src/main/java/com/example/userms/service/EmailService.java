package com.example.userms.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(body, true);
        javaMailSender.send(mimeMessage);

    }

    public void sendVerificationEmail(String email, String otp) throws MessagingException {
        String subject = "Email Verification";
        String body = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Email Verification</title><style>body {font-family: Arial, sans-serif;background-color: #f4f4f4;margin: 0;padding: 0;}.container {max-width: 600px;margin: 0 auto;padding: 20px;background-color: #ffffff;border-radius: 8px;box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);}.header {background-color: #007bff;color: #ffffff;padding: 20px;text-align: center;border-top-left-radius: 8px;border-top-right-radius: 8px;}.content {padding: 20px;}.otp {font-size: 24px;color: #333333;text-align: center;margin-top: 20px;}</style></head><body><div class=\"container\"><div class=\"header\"><h2>Email Verification</h2></div><div class=\"content\"><p>Thank you for signing up! To verify your email, please use the following OTP</p><div class=\"otp\"><strong>" + otp + "</strong></div></div></div></body></html>";
        sendEmail(email, subject, body);
    }

}
