package org.example.otpgenerator.service;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender mailSender;


    public Boolean sendEmail(String toEmail, String otp) {
        try {
            log.info("Attempting to send email to: {}", toEmail);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("SALAM!");
            helper.setTo(toEmail);
            helper.setSubject("Your Verification Code");
            helper.setText(String.format(
                    "Your verification code is: <b>%s</b><br>This code expires in 5 minutes.",
                    otp
            ), true);

            mailSender.send(message);
            log.info("Email successfully sent to: {}", toEmail);
            return true;
        } catch (Exception e) {
            log.error("email service error", e);
            return false;
        }
    }
}

