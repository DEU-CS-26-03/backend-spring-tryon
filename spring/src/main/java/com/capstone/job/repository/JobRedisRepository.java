package com.capstone.job.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class JobRedisRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String STATUS_KEY   = "tryon:status:";
    private static final String PROGRESS_KEY = "tryon:progress:";
    private static final long   DEFAULT_TTL  = 3600L; // 1시간

    public void save(String tryonId, String status, int progress) {
        redisTemplate.opsForValue()
                .set(STATUS_KEY + tryonId, status, DEFAULT_TTL, TimeUnit.SECONDS);
        redisTemplate.opsForValue()
                .set(PROGRESS_KEY + tryonId, String.valueOf(progress), DEFAULT_TTL, TimeUnit.SECONDS);
    }

    public String findStatusById(String tryonId) {
        return redisTemplate.opsForValue().get(STATUS_KEY + tryonId);
    }

    public Integer findProgressById(String tryonId) {
        String val = redisTemplate.opsForValue().get(PROGRESS_KEY + tryonId);
        if (val == null) return null;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void expire(String tryonId, long seconds) {
        redisTemplate.expire(STATUS_KEY   + tryonId, seconds, TimeUnit.SECONDS);
        redisTemplate.expire(PROGRESS_KEY + tryonId, seconds, TimeUnit.SECONDS);
    }

    public void delete(String tryonId) {
        redisTemplate.delete(STATUS_KEY   + tryonId);
        redisTemplate.delete(PROGRESS_KEY + tryonId);
    }
}