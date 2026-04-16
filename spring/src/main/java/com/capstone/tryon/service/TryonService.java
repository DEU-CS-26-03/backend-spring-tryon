package com.capstone.tryon.service;

import com.capstone.job.repository.JobRedisRepository;
import com.capstone.tryon.dto.TryonCreateRequest;
import com.capstone.tryon.dto.TryonErrorInfo;
import com.capstone.tryon.dto.TryonResponse;
import com.capstone.tryon.entity.TryonJob;
import com.capstone.tryon.repository.TryonJobRepository;
import com.capstone.user.entity.User;
import com.capstone.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TryonService {

    private static final Logger log = LoggerFactory.getLogger(TryonService.class);

    private final TryonJobRepository tryonJobRepository;
    private final JobRedisRepository jobRedisRepository;
    private final UserRepository userRepository;

    @Transactional
    public TryonResponse create(TryonCreateRequest request, String email) {
        if (request.getGarmentId() == null && request.getExternalItemKey() == null) {
            throw new IllegalArgumentException("garmentId 또는 externalItemKey 중 하나는 필수입니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String tryonId = "tryon_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        TryonJob job = new TryonJob();
        job.setTryonId(tryonId);
        job.setUserId(user.getId());
        job.setStatus("queued");
        job.setProgress(0);
        job.setUserImageId(request.getUserImageId());
        job.setGarmentId(request.getGarmentId());
        job.setExternalItemKey(request.getExternalItemKey());

        tryonJobRepository.save(job);

        try {
            jobRedisRepository.save(tryonId, "queued", 0);
        } catch (Exception e) {
            log.warn("[Redis] 캐시 저장 실패 (tryonId={}): {}", tryonId, e.getMessage());
        }

        TryonResponse res = toResponse(job);
        res.setMessage("가상 피팅 작업이 생성되었습니다.");
        return res;
    }

    @Transactional(readOnly = true)
    public List<TryonResponse> listByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return tryonJobRepository.findByUserIdAndDeletedFalseOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TryonResponse getById(String tryonId) {
        try {
            String cachedStatus = jobRedisRepository.findStatusById(tryonId);
            if (cachedStatus != null &&
                    ("queued".equals(cachedStatus) || "processing".equals(cachedStatus))) {
                Integer progress = jobRedisRepository.findProgressById(tryonId);
                return toPartialResponse(tryonId, cachedStatus, progress != null ? progress : 0);
            }
        } catch (Exception e) {
            log.warn("[Redis] 캐시 조회 실패 (tryonId={}): {}", tryonId, e.getMessage());
        }

        TryonJob job = tryonJobRepository.findById(tryonId)
                .orElseThrow(() -> new IllegalArgumentException("해당 작업을 찾을 수 없습니다: " + tryonId));

        try {
            jobRedisRepository.save(tryonId, job.getStatus(), job.getProgress());
        } catch (Exception e) {
            log.warn("[Redis] 캐시 갱신 실패 (tryonId={}): {}", tryonId, e.getMessage());
        }

        return toResponse(job);
    }

    @Transactional
    public void softDelete(String tryonId) {
        TryonJob job = tryonJobRepository.findById(tryonId)
                .orElseThrow(() -> new IllegalArgumentException("해당 작업을 찾을 수 없습니다: " + tryonId));
        job.setDeleted(true);
        tryonJobRepository.save(job);
    }

    @Transactional
    public TryonResponse updateStatus(String tryonId, String status, int progress,
                                      String resultId, String errorCode, String errorMessage) {
        TryonJob job = tryonJobRepository.findById(tryonId)
                .orElseThrow(() -> new IllegalArgumentException("해당 작업을 찾을 수 없습니다: " + tryonId));

        job.setStatus(status);
        job.setProgress(progress);
        if (resultId != null) job.setResultId(resultId);
        if (errorCode != null) {
            job.setErrorCode(errorCode);
            job.setErrorMessage(errorMessage);
        }
        tryonJobRepository.save(job);

        try {
            jobRedisRepository.save(tryonId, status, progress);
            if ("completed".equals(status) || "failed".equals(status)) {
                jobRedisRepository.expire(tryonId, 60);
            }
        } catch (Exception e) {
            log.warn("[Redis] 상태 동기화 실패 (tryonId={}): {}", tryonId, e.getMessage());
        }

        return toResponse(job);
    }

    @Transactional
    public Optional<TryonResponse> claimNextPendingJob() { //FIFO 보장
        Optional<TryonJob> job = tryonJobRepository.findFirstByStatusInOrderByCreatedAtAsc(
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

    private TryonResponse toPartialResponse(String tryonId, String status, int progress) {
        TryonResponse res = new TryonResponse();
        res.setTryonId(tryonId);
        res.setStatus(status);
        res.setProgress(progress);
        return res;
    }
}