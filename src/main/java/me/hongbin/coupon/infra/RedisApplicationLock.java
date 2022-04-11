package me.hongbin.coupon.infra;

import static java.time.Duration.*;

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
    public void lock(Long userId) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Long count = valueOperations.increment(key(userId));

        if (count > 2) {
            throw new IllegalStateException("동시에 접근했습니다");
        }

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
