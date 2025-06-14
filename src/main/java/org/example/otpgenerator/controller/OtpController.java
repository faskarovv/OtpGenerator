package org.example.otpgenerator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.otpgenerator.Dto.RequestDto;
import org.example.otpgenerator.Dto.ResponseDto;
import org.example.otpgenerator.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {


    private final OtpService otpService;

    @PostMapping("/generate")
    public ResponseEntity<ResponseDto> sendOtp(@Valid @RequestBody RequestDto request) {

        ResponseDto response = otpService.sendOtp(request.getEmail());

        return ResponseEntity.ok(response);
    }
}
