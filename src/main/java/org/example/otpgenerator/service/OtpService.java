package org.example.otpgenerator.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otpgenerator.Dto.ResponseDto;
import org.example.otpgenerator.entity.Card;
import org.example.otpgenerator.entity.Status;
import org.example.otpgenerator.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;



@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private final TokenService tokenService;
    private final EmailService emailService;
    private final TokenStorageService tokenStorageService;
    private final UserRepository userRepository;



    public ResponseDto sendOtp(String email) {
        try {
            String otp = generateAndStoreOtp(email);

            if(!sendOtpEmail(email , otp)){
                tokenStorageService.removeToken(email);
                log.error("could not delete nor send email for email {} " , email);
                return new ResponseDto("Failed to sent email" , email);
            }
            saveUserToRepo(email);
            return new ResponseDto("send otp and saved the card" , email);
        }catch (Exception e) {
            log.error("Error sending OTP to {}:", email);
            return new ResponseDto("internal error", email);
        }
    }

    private boolean sendOtpEmail(String email, String otp) {
        try{
            CompletableFuture<Boolean> emailSent = emailService.sendEmail(email, otp);

                if (!emailSent.get()) {
                    log.error("Email sending failed for {}", email);
                }
                return emailSent.get();
        }catch (Exception e) {
            log.error("Exception sending email to {}: ", email);
            return false;
        }
    }

    private String generateAndStoreOtp(String email) {
        try {
            String otp = tokenService.generateOtp();
            String hashesOtp = tokenService.generateOtpHash(otp);
            log.info("generated otp and hashed otp");

            tokenStorageService.storeToken(email, hashesOtp);
            log.info("stored otp in redis with key {} ", email);

            return otp;
        } catch (RuntimeException e) {
            log.info("could not generate token");
            throw new RuntimeException(e);
        }
    }

    public void saveUserToRepo(String email) {
        if (userRepository.existsCardByEmail(email).isPresent()) {
            log.info("User with email {} already exists", email);
            return;
        }

        try {
            Card newCard = Card.builder()
                    .email(email)
                    .status(Status.INACTIVE.toString())
                    .build();

            userRepository.save(newCard);
            log.info("saved user for the email {} ", email);
        } catch (RuntimeException e) {
            log.error("could not save card for email {} ", email);
            throw new RuntimeException();
        }
    }
}
