package com.capstone.tryon.service;

import com.capstone.job.repository.JobRedisRepository;
import com.capstone.tryon.dto.*;
import com.capstone.tryon.entity.TryonJob;
import com.capstone.tryon.repository.TryonJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
public class TryonService {

    private static final Logger log = LoggerFactory.getLogger(TryonService.class);

    private final TryonJobRepository tryonJobRepository;   // MariaDB - 영구 저장
    private final JobRedisRepository jobRedisRepository;   // Redis  - 상태 캐시

    public TryonService(TryonJobRepository tryonJobRepository,
                        JobRedisRepository jobRedisRepository) {
        this.tryonJobRepository = tryonJobRepository;
        this.jobRedisRepository = jobRedisRepository;
    }

    @Transactional
    public TryonResponse create(TryonCreateRequest request) {
        // MVP: queued / processing 상태 작업이 이미 있으면 409
        Optional<TryonJob> activeJob = tryonJobRepository.findFirstByStatusIn(
                Arrays.asList("queued", "processing")
        );
        if (activeJob.isPresent()) {
            throw new IllegalStateException("ALREADY_ACTIVE");
        }

        // 1. MariaDB에 TryonJob 영구 저장
        String tryonId = "tryon_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        TryonJob job = new TryonJob();
        job.setTryonId(tryonId);
        job.setStatus("queued");
        job.setProgress(0);
        job.setUserImageId(request.getUserImageId());
        job.setGarmentId(request.getGarmentId());

        tryonJobRepository.save(job);

        // 2. Redis에 상태 캐시 저장 (빠른 polling 응답용)
        try {
            jobRedisRepository.save(tryonId, "queued", 0);
        } catch (Exception e) {
            // Redis 장애 시 DB 저장은 유지 - 로그만 남기고 계속 진행
            log.warn("[Redis] 캐시 저장 실패 (tryonId={}): {}", tryonId, e.getMessage());
        }

        TryonResponse res = toResponse(job);
        res.setMessage("Try-on job created successfully.");
        return res;
    }

    public TryonResponse getById(String tryonId) {

        // 1차: Redis 캐시 조회
        try {
            String cachedStatus = jobRedisRepository.findStatusById(tryonId);
            if (cachedStatus != null) {
                // 캐시 히트: DB 조회 없이 Redis 정보로 바로 응답
                // 단, completed / failed 등 최종 상태는 DB에서 전체 정보 조회
                if ("queued".equals(cachedStatus) || "processing".equals(cachedStatus)) {
                    Integer cachedProgress = jobRedisRepository.findProgressById(tryonId);
                    return toPartialResponse(tryonId, cachedStatus,
                            cachedProgress != null ? cachedProgress : 0);
                }
            }
        } catch (Exception e) {
            log.warn("[Redis] 캐시 조회 실패 (tryonId={}): {}", tryonId, e.getMessage());
        }

        // 2차: DB fallback
        TryonJob job = tryonJobRepository.findById(tryonId)
                .orElseThrow(() -> new IllegalArgumentException("해당 작업을 찾을 수 없습니다: " + tryonId));

        // Redis 캐시 갱신 (DB에서 읽었으니 다시 올려둠)
        try {
            jobRedisRepository.save(tryonId, job.getStatus(), job.getProgress());
        } catch (Exception e) {
            log.warn("[Redis] 캐시 갱신 실패 (tryonId={}): {}", tryonId, e.getMessage());
        }

        return toResponse(job);
    }

    @Transactional
    public TryonResponse updateStatus(String tryonId, String status, int progress,
                                      String resultId, String errorCode, String errorMessage) {
        TryonJob job = tryonJobRepository.findById(tryonId)
                .orElseThrow(() -> new IllegalArgumentException("해당 작업을 찾을 수 없습니다: " + tryonId));

        // MariaDB 업데이트
        job.setStatus(status);
        job.setProgress(progress);
        if (resultId != null) job.setResultId(resultId);
        if (errorCode != null) {
            job.setErrorCode(errorCode);
            job.setErrorMessage(errorMessage);
        }
        tryonJobRepository.save(job);

        // Redis 캐시 동기화
        try {
            jobRedisRepository.save(tryonId, status, progress);

            // 최종 상태(completed / failed)는 캐시 TTL 짧게 or 삭제
            if ("completed".equals(status) || "failed".equals(status)) {
                jobRedisRepository.expire(tryonId, 60); // 60초 후 Redis 캐시 만료
            }
        } catch (Exception e) {
            log.warn("[Redis] 상태 동기화 실패 (tryonId={}): {}", tryonId, e.getMessage());
        }

        return toResponse(job);
    }

    @Transactional
    public Optional<TryonResponse> claimNextPendingJob() {
        Optional<TryonJob> job = tryonJobRepository.findFirstByStatusIn(
                Arrays.asList("queued")
        );

        job.ifPresent(j -> {
            j.setStatus("processing");
            j.setProgress(0);
            tryonJobRepository.save(j);

            try {
                jobRedisRepository.save(j.getTryonId(), "processing", 0);
            } catch (Exception e) {
                log.warn("[Redis] processing 상태 캐시 실패: {}", e.getMessage());
            }
        });

        return job.map(this::toResponse);
    }


    // DB 전체 정보로 변환
    private TryonResponse toResponse(TryonJob j) {
        TryonResponse res = new TryonResponse();
        res.setTryonId(j.getTryonId());
        res.setStatus(j.getStatus());
        res.setProgress(j.getProgress());
        res.setUserImageId(j.getUserImageId());
        res.setGarmentId(j.getGarmentId());
        res.setResultId(j.getResultId());
        res.setCreatedAt(j.getCreatedAt());
        res.setUpdatedAt(j.getUpdatedAt());

        if (j.getErrorCode() != null) {
            res.setError(new TryonErrorInfo(j.getErrorCode(), j.getErrorMessage()));
        }
        return res;
    }

    // Redis 캐시 히트 시 일부 정보로만 응답 (polling 최적화)
    private TryonResponse toPartialResponse(String tryonId, String status, int progress) {
        TryonResponse res = new TryonResponse();
        res.setTryonId(tryonId);
        res.setStatus(status);
        res.setProgress(progress);
        return res;
    }
}