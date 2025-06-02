package org.example.otpgenerator.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otpgenerator.entity.Otp;
import org.example.otpgenerator.service.TokenStorogeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/internal")
@AllArgsConstructor
public class InternalController {

    private TokenStorogeService tokenStorogeService;

    @GetMapping("/fetch-token/{email}")
    public ResponseEntity<Otp.TokenResponse> fetchToken(@PathVariable String email) {
        String token = tokenStorogeService.getToken(email);
        if (token == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(new Otp.TokenResponse(token));
    }

    @PostMapping("/remove-token/{email}")
    public ResponseEntity<?> removeToken(@PathVariable String email){

        tokenStorogeService.removeToken(email);

        return ResponseEntity.ok("removed");
    }
}
