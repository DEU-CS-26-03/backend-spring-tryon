package com.capstone.internal.service;

import com.capstone.garment.entity.Garment;
import com.capstone.garment.repository.GarmentRepository;
import com.capstone.internal.dto.InternalJobResponse;
import com.capstone.internal.dto.InternalJobStatusRequest;
import com.capstone.tryon.dto.TryonResponse;
import com.capstone.tryon.entity.TryonJob;
import com.capstone.tryon.repository.TryonJobRepository;
import com.capstone.tryon.service.TryonService;
import com.capstone.userimage.entity.UserImage;
import com.capstone.userimage.repository.UserImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InternalJobService {

    private final TryonService tryonService;
    private final TryonJobRepository tryonJobRepository;
    private final UserImageRepository userImageRepository;
    private final GarmentRepository garmentRepository;

    /**
     * 다음 queued 작업을 claim하고 파일 경로까지 반환
     */
    @Transactional
    public Optional<InternalJobResponse> claimNextJob() {
        Optional<TryonResponse> claimed = tryonService.claimNextPendingJob();
        if (claimed.isEmpty()) return Optional.empty();

        TryonResponse tryon = claimed.get();
        TryonJob job = tryonJobRepository.findById(tryon.getTryonId()).orElseThrow();

        // 실제 fileUrl 조회 (단순 ID 조합이 아닌 DB에서 가져옴)
        String userImagePath = userImageRepository.findById(job.getUserImageId())
                .map(UserImage::getFileUrl)
                .orElse("/files/user-images/" + job.getUserImageId());

        String garmentPath = null;
        if (job.getGarmentId() != null) {
            garmentPath = garmentRepository.findById(job.getGarmentId())
                    .map(Garment::getFileUrl)
                    .orElse("/files/garments/" + job.getGarmentId());
        }

        return Optional.of(new InternalJobResponse(
                tryon.getTryonId(),
                userImagePath,
                garmentPath,
                tryon.getStatus()
        ));
    }

    /**
     * Python worker가 처리 상태/결과를 보고
     */
    @Transactional
    public TryonResponse reportStatus(String tryonId, InternalJobStatusRequest request) {
        return tryonService.updateStatus(
                tryonId,
                request.getStatus(),
                request.getProgress(),
                request.getResultId(),
                request.getErrorCode(),
                request.getErrorMessage()
        );
    }
}