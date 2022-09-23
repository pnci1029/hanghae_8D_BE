//package com.example.checkcheck.service;
//
//import com.example.checkcheck.exception.CustomException;
//import com.example.checkcheck.exception.ErrorCode;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.util.concurrent.TimeUnit;
//
//@RequiredArgsConstructor
//@Service
//public class RedisService {
//    private final RedisTemplate<String, String> redisTemplate;
//
//    public void setValues(String key, String data) {
//        ValueOperations<String, String> values = redisTemplate.opsForValue();
//        values.set(key, data);
//        redisTemplate.expire(key, 60, TimeUnit.MINUTES);
//    }
//
//    public void setValues(String key, String data, Duration duration) {
//        ValueOperations<String, String> values = redisTemplate.opsForValue();
//        values.set(key, data, duration);
//    }
//
//    public String getValues(String key) {
//        ValueOperations<String, String> values = redisTemplate.opsForValue();
//        return values.get(key);
//    }
//
//    public void deleteValues(String key) {
//        redisTemplate.delete(key);
//    }
//
//    public void checkRefreshToken(String username, String refreshToken) {
//        String redisRT = this.getValues(username);
//        if(!refreshToken.equals(redisRT)) {
//            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
//        }
//    }
//}
