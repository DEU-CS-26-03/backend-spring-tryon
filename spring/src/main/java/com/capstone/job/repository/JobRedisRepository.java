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

    /**
     * TryonService용 통합 저장 메서드
     * - status + progress를 한 번에 Hash에 저장
     * - Key가 없으면 새로 생성, 있으면 두 필드만 덮어씀
     * - TTL은 기본 1시간 적용
     */
    public void save(String jobId, String status, int progress) {
        String key = KEY_PREFIX + jobId;
        redisTemplate.opsForHash().put(key, "status", status);
        redisTemplate.opsForHash().put(key, "progress", String.valueOf(progress));
        // Key가 새로 만들어진 경우에만 TTL 설정 (기존 TTL 초기화 방지)
        Boolean hasExpire = redisTemplate.getExpire(key) > 0L;
        if (!hasExpire) {
            redisTemplate.expire(key, TTL);
        }
    }

    /**
     * status 조회 (TryonService의 Redis 1차 조회용)
     * - 없으면 null 반환 → TryonService에서 DB fallback으로 전환됨
     */
    public String findStatusById(String jobId) {
        Object status = redisTemplate.opsForHash().get(KEY_PREFIX + jobId, "status");
        return status != null ? status.toString() : null;
    }

    /**
     * progress 조회
     * - 없으면 null 반환 → TryonService에서 0으로 처리됨
     */
    public Integer findProgressById(String jobId) {
        Object progress = redisTemplate.opsForHash().get(KEY_PREFIX + jobId, "progress");
        if (progress == null) return null;
        try {
            return Integer.parseInt(progress.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * TTL 설정 (completed / failed 최종 상태 캐시 만료용)
     * - TryonService에서 최종 상태 전환 시 짧은 TTL(60초)로 줄여서 자동 정리
     */
    public void expire(String jobId, long seconds) {
        redisTemplate.expire(KEY_PREFIX + jobId, Duration.ofSeconds(seconds));
    }
}