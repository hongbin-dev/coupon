package me.hongbin.coupon.infra;

import static java.time.Duration.*;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import me.hongbin.coupon.core.ApplicationLock;

@Component
public class RedisApplicationLock implements ApplicationLock {

    private final StringRedisTemplate redisTemplate;

    public RedisApplicationLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isLocked(Long userId) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String isLocked = valueOperations.get(key(userId));

        return Boolean.getBoolean(isLocked);
    }

    @Override
    public void lock(Long userId) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key(userId), "true");
        redisTemplate.expire(key(userId), ofMillis(10000));
    }

    @Override
    public void wake(Long userId) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key(userId), "false");
    }

    private String key(Long userId) {
        return String.format("lock-%d", userId);
    }
}
