package com.hr.onboard.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.Optional;

public class RedisServiceImpl implements RedisService {

    @Autowired private StringRedisTemplate redisTemplate;

    @Override
    public void set(String key, String value, Duration duration) {
        redisTemplate.opsForValue().setIfAbsent(key, value, duration);
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    @Override
    public boolean has(String key) {
        return redisTemplate.hasKey(key);
    }
}
