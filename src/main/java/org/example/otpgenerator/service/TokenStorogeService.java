package org.example.otpgenerator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenStorogeService {

    private final RedisTemplate<String , String> tokenStorage ;

    public void storeToken(String email, String token ) {
        long expirationTime = 5;
        tokenStorage.opsForValue().set(email , token , expirationTime , TimeUnit.MINUTES);
        log.info("token stored for email: {}", email);
    }

    //delete if not needed fyi
    public String getToken(String email) {
        String token = tokenStorage.opsForValue().get(email);
        log.info("token for the email: {}", email);
        return token;
    }

    public void removeToken(String email) {
       Boolean removed = tokenStorage.delete(email);
       if(Boolean.TRUE.equals(removed)) {
           log.info("token for email:{}removed", email);
       }else{
           log.warn("could not remove the token" , email);
       }
    }
}
