package me.hongbin.coupon.infra;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import me.hongbin.coupon.core.ApplicationCounter;

@Component
public class RedisApplicationCounter implements ApplicationCounter {

    private final StringRedisTemplate redisTemplate;

    public RedisApplicationCounter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public long increment(Long userId) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.increment(key(userId));
    }

    private String key(Long userId) {
        return String.format("counter-%d", userId);
    }
}
