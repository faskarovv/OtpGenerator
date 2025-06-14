package org.example.otpgenerator.service;

import org.example.otpgenerator.Dto.ResponseDto;
import org.example.otpgenerator.entity.Card;
import org.example.otpgenerator.entity.Status;
import org.example.otpgenerator.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OtpServiceTest {
    @Mock
    private TokenService tokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private TokenStorageService tokenStorageService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OtpService otpService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void succeedToSendOtp(){
        String email = "email@gmail.com";
        String otp = "1234";
        String hashedOtp = "hashed-1234";

        when(tokenService.generateOtp()).thenReturn(otp);
        when(tokenService.generateOtpHash(otp)).thenReturn(hashedOtp);
        when(emailService.sendEmail(email , otp)).thenReturn(CompletableFuture.completedFuture(true));
        when(userRepository.existsCardByEmail(email)).thenReturn(Optional.empty());


        ResponseDto responseDto = otpService.sendOtp(email);


        assertEquals("send otp and saved the card", responseDto.getMessage());
        verify(tokenStorageService).storeToken(email, hashedOtp);
        verify(userRepository).save(any(Card.class));
    }

    @Test
    void failedToSendOtp_Email(){
        String email = "email@gmail.com";
        String otp = "1234";
        String hashedOtp = "hashed-1234";

        when(tokenService.generateOtp()).thenReturn(otp);
        when(tokenService.generateOtpHash(otp)).thenReturn(hashedOtp);
        when(emailService.sendEmail(email , otp)).thenReturn(CompletableFuture.completedFuture(false));

        ResponseDto responseDto = otpService.sendOtp(email);

        assertEquals("Failed to sent email", responseDto.getMessage());
        verify(tokenStorageService).storeToken(email, hashedOtp);

    }

    @Test
    void internalError(){
        String email = "email@gmail.com";

        when(tokenService.generateOtp()).thenThrow(new RuntimeException());

        ResponseDto responseDto = otpService.sendOtp(email);

        assertEquals("internal error" , responseDto.getMessage());
        verify(tokenService , times(1)).generateOtp();
    }

    @Test
    void emailError(){
        String email = "email@gmail.com";
        String otp = "1234";


        when(tokenService.generateOtp()).thenReturn(otp);
        when(emailService.sendEmail(email , otp)).thenThrow(new RuntimeException());

        ResponseDto responseDto = otpService.sendOtp(email);

        verify(emailService , times(1)).sendEmail(email , otp);
    }

    @Test
    void userExistsWithGivenEmail(){
        String email = "email@gmail.com";
        String otp = "1234";
        String hashedOtp = "hashed-1234";

        Card exisitngCard = new Card();
        exisitngCard.setEmail(email);
        exisitngCard.setStatus(Status.INACTIVE.toString());

        when(userRepository.existsCardByEmail(email)).thenReturn(Optional.of(exisitngCard));

        otpService.saveUserToRepo(email);

        verify(userRepository, never()).save(any(Card.class));

    }

    @Test
    void saveUserToRepo_Error(){
        String email = "email@gmail.com";
        String otp = "1234";
        String hashedOtp = "hashed-1234";

        when(userRepository.existsCardByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(Card.class))).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> otpService.saveUserToRepo(email));

        verify(userRepository, times(1)).save(any(Card.class));
    }
}