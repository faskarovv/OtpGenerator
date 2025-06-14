package org.example.otpgenerator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;


class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateOtpSuccess() {
        String otp = tokenService.generateOtp();

        assertNotNull(otp);
        assertEquals(4, otp.length());
        assertTrue(otp.matches("\\d{4}"));
    }

    @Test
    void generateOtpHashSuccess() {
        String otp = "1234";
        String otpHashed = tokenService.generateOtpHash(otp);

        assertNotNull(otpHashed);
    }

    @Test
    void generateOtpHashFailure() {
        assertThrows(RuntimeException.class, () -> {
            tokenService.generateOtpHash(null);
        });
    }
}