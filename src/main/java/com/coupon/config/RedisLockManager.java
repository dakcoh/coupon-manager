package com.coupon.config;

import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Slf4j
public class RedisLockManager {

    private final StringRedisTemplate redis;

    // 락 해제 시 소유자 검증을 원자적으로 수행하는 Lua
    private static final String RELEASE_LUA =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "  return redis.call('del', KEYS[1]) " +
                    "else return 0 end";

    public RedisLockManager(StringRedisTemplate redis) {
        this.redis = redis;
    }

    // 락 획득
    public String acquire(String key, Duration ttl) {
        String token = UUID.randomUUID().toString();
        Boolean ok = redis.opsForValue().setIfAbsent(key, token, ttl);
        return Boolean.TRUE.equals(ok) ? token : null;
    }

    // 락 해제
    public void release(String key, String token) {
        try {
            redis.execute(
                    new DefaultRedisScript<>(RELEASE_LUA, Long.class),
                    java.util.Collections.singletonList(key),
                    token
            );
        } catch (DataAccessException e) {
            log.warn("lock.release error key={} token8={} msg={}", key, safe(token), e.getMessage(), e);
        }
    }

    private static String safe(String token) {
        return token == null ? "null" : token.substring(0, Math.min(8, token.length()));
    }
}
