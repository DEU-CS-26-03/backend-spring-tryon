package com.capstone.job.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JobRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "job:";
    private static final Duration TTL = Duration.ofHours(1);

    // Job 생성 (최초 저장)
    public void createJob(String jobId, Long userId) {
        String key = KEY_PREFIX + jobId;
        Map<String, Object> jobData = Map.of(
                "status", "QUEUED",
                "userId", userId.toString(),
                "createdAt", java.time.LocalDateTime.now().toString()
        );
        redisTemplate.opsForHash().putAll(key, jobData);
        redisTemplate.expire(key, TTL);
    }

    // 상태만 업데이트
    public void updateStatus(String jobId, String status) {
        String key = KEY_PREFIX + jobId;
        redisTemplate.opsForHash().put(key, "status", status);
    }

    // 완료 시 결과 URL 저장
    public void setCompleted(String jobId, String resultUrl) {
        String key = KEY_PREFIX + jobId;
        redisTemplate.opsForHash().put(key, "status", "COMPLETED");
        redisTemplate.opsForHash().put(key, "resultUrl", resultUrl);
    }

    // Job 전체 조회
    public Map<Object, Object> getJob(String jobId) {
        return redisTemplate.opsForHash().entries(KEY_PREFIX + jobId);
    }

    // 상태만 조회
    public String getStatus(String jobId) {
        Object status = redisTemplate.opsForHash().get(KEY_PREFIX + jobId, "status");
        return status != null ? status.toString() : null;
    }

    // Job 삭제 (수동)
    public void deleteJob(String jobId) {
        redisTemplate.delete(KEY_PREFIX + jobId);
    }
}