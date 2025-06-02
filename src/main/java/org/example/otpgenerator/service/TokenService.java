package org.example.otpgenerator.service;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


@Data
@Slf4j
@Service
public class TokenService {

    public String generateOtp() {
        String otp = RandomStringUtils.randomNumeric(4);

        return otp;
    }

    public String generateOtpHash(String otp) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashedBytes = digest.digest(otp.getBytes());

            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("no such algorithm exists ", e.getMessage());
        }
        return "could not generate";
    }
}
