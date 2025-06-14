package org.example.otpgenerator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenStorageService {

    private final RedisTemplate<String, String> redisTemplate; //redisTemplate

    public void storeToken(String email, String token) {
        if (token == null) {
            log.error("token cannot be null");
            throw new IllegalArgumentException();
        }
        long expirationTime = 5;
        redisTemplate.opsForValue().set(email, token, expirationTime, TimeUnit.MINUTES);
        log.info("token stored for email: {}", email);
    }

    public boolean removeToken(String email) {
        Boolean removed = redisTemplate.delete(email);
        if (removed) {
            log.info("token for email: {} removed", email);
        } else {
            log.warn("could not remove the token for email: {}", email);
        }
        return removed;
    }
}
