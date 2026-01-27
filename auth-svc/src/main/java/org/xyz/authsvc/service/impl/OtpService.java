package org.xyz.authsvc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long OTP_EXPIRY_FIVE_MIN = 300;

    public void validateOtp(String emailIdentifier,
                            String otpRequest) {
        String key = emailIdentifier;
        String otpCache = redisTemplate.opsForValue().get(key);

        System.out.println("myKey: " + key);
        if (otpCache == null) {
            throw new IllegalStateException("OTP session expired");
        }

        if (!otpCache.equals(otpRequest)) {
            throw new IllegalStateException("OTP does not match");
        }
    }

    public void cacheOtp(String emailIdentifier, String otp) {
        String key = emailIdentifier;
        redisTemplate.opsForValue().set(key, otp, OTP_EXPIRY_FIVE_MIN, TimeUnit.SECONDS);
    }

    public String retrieveCacheOtp(String emailIdentifier) {
        String key = emailIdentifier;
        long expiry = redisTemplate.getExpire(key);
        return redisTemplate.opsForValue().get(key) + expiry;
    }

    public void deleteCacheOtp(String emailIdentifier) {
        String key = emailIdentifier;
        redisTemplate.opsForValue().get(key);
    }
}
