package org.example.otpgenerator.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otpgenerator.entity.Otp;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class OtpService {

    private TokenService tokenService;
    private EmailService emailService;
    private TokenStorogeService tokenStorogeService;

    public Otp.Response sendOtp(String email) {
        try {
//            if (tokenStorogeService.getToken(email) != null) {
//                tokenStorogeService.removeToken(email);
//                log.info("Removed existing otp for {}", email);
//            }
            String otp = tokenService.generateOtp();
            String otpHash = tokenService.generateOtpHash(otp);
            log.info("Generated otp for {}", email);

            tokenStorogeService.storeToken(email, otpHash);

            try {
                boolean emailSent = emailService.sendEmail(email, otp);

                if (!emailSent) {
                    log.error("Email sending failed for {}", email);
                    tokenStorogeService.removeToken(email);
                    return new Otp.Response("Email service unavailable", email);
                }

                return new Otp.Response("Otp sent", email);

            } catch (Exception emailException) {
                log.error("Failed to send email to {}: {}", email, emailException.getMessage());
                tokenStorogeService.removeToken(email);
                return new Otp.Response("Email service unavailable", email);
            }

        } catch (RuntimeException e) {
            log.error("could not send otp", e.getMessage());

        }
        return new Otp.Response("Failed to generate otp", "");
    }
}
