package org.example.otpgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OtpGeneratorApplication {

    public static void main(String[] args) {

        SpringApplication.run(OtpGeneratorApplication.class, args);
    }

}
