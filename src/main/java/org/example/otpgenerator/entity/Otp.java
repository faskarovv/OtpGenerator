package org.example.otpgenerator.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Otp {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Email(message = "Invalid email")
        private String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String message;
        private String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenResponse {
        @JsonProperty("jwtToken")
        private String jwtToken;
    }

}
