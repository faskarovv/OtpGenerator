package org.example.otpgenerator.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otpgenerator.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.otpgenerator.entity.Otp;

@Slf4j
@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {


    private final OtpService otpService;

    @PostMapping("/generate")
    public ResponseEntity<Otp.Response> generateOtp(@Valid @RequestBody Otp.Request request) {

        Otp.Response response = otpService.sendOtp(request.getEmail());

        return ResponseEntity.ok(response);
    }
}
