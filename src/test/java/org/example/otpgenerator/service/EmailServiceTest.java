package org.example.otpgenerator.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void emailServerError() throws ExecutionException, InterruptedException {
        String email = "email@gmail.com";
        String otp = "1234";


        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailException("mail exception") {
        }).when(javaMailSender).send(mimeMessage);

        CompletableFuture<Boolean> emailSent = emailService.sendEmail(email, otp);

        assertFalse(emailSent.get());
        verify(javaMailSender, times(1)).send(mimeMessage);

    }

    @Test
    void emailServiceSuccess() throws ExecutionException, InterruptedException {
        String email = "email@gmail.com";
        String otp = "1234";

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        CompletableFuture<Boolean> emailSent = emailService.sendEmail(email, otp);

        assertTrue(emailSent.get());
        verify(javaMailSender , times(1)).send(mimeMessage);
    }
}