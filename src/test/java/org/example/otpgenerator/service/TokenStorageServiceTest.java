package org.example.otpgenerator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenStorageServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;


    @InjectMocks
    private TokenStorageService tokenStorageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void successfullyStoredToken() {
        String email = "email@gmail.com";
        String token = "1234";
        long expTime = 5;

        tokenStorageService.storeToken(email, token);

        verify(valueOperations, times(1))
                .set(email, token, expTime, TimeUnit.MINUTES);
    }

    @Test
    void failedToStoreTokenWithNull() {
        assertThrows(IllegalArgumentException.class, () ->
                tokenStorageService.storeToken("email", null));
    }

    @Test
    void successfullyRemovedToken() {
        String email = "email@gmail.com";

        when(redisTemplate.delete(email)).thenReturn(true);

        boolean isDeleted = tokenStorageService.removeToken(email);
        assertTrue(isDeleted);

       /* verify(redisTemplate, times(1))
                .delete(email);*/
    }

    @Test
    void couldNotRemoveToken() {
        String email = "email@gmail.com";

        when(redisTemplate.delete(email)).thenReturn(false);

        boolean isDeleted = tokenStorageService.removeToken(email);

        assertFalse(isDeleted);

        /*verify(redisTemplate, times(1))
                .delete(email);*/
    }

}