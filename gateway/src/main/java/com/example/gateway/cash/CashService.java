package com.example.gateway.cash;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CashService {
    private final RedisTemplate<String, String> redisTemplate;
    private final Long TTL = 10L;

    public boolean hasKey (String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void addToCash(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, TTL, TimeUnit.MINUTES);
    }

    public String getFromCash (String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteFromCash(String key) {
        redisTemplate.delete(key);
    }
}
