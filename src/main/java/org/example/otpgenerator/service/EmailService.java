package org.example.otpgenerator.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public CompletableFuture<Boolean> sendEmail(String toEmail, String otp) {
        try {
            log.info("Attempting to send email to: {}", toEmail);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("fasgerov06@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Your Verification Code");
            helper.setText(String.format(
                    "Your verification code is: <b>%s</b><br>This code expires in 5 minutes.",
                    otp
            ), true);

            log.info("About to send email");
            mailSender.send(message);
            log.info("Email successfully sent to: {}", toEmail);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            log.error("email service error", e);
            return CompletableFuture.completedFuture(false);
        }
    }
}