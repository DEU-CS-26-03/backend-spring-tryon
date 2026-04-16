package com.capstone.internal.controller;

import com.capstone.internal.dto.InternalJobResponse;
import com.capstone.internal.dto.InternalJobStatusRequest;
import com.capstone.internal.service.InternalJobService;
import com.capstone.tryon.dto.TryonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/internal/jobs")
@RequiredArgsConstructor
public class InternalJobController {

    private final InternalJobService internalJobService;

    // GET /api/internal/jobs/next — 다음 작업 claim
    @GetMapping("/next")
    public ResponseEntity<?> claimNextJob() {
        Optional<InternalJobResponse> job = internalJobService.claimNextJob();

        if (job.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 — 대기 작업 없음
        }
        return ResponseEntity.ok(job.get());
    }

    // PATCH /api/internal/jobs/{tryonId}/status — 상태/결과 보고
    @PatchMapping("/{tryonId}/status")
    public ResponseEntity<TryonResponse> updateStatus(
            @PathVariable String tryonId,
            @RequestBody InternalJobStatusRequest request
    ) {
        return ResponseEntity.ok(internalJobService.reportStatus(tryonId, request));
    }

    // GET /api/internal/jobs/health — 연결 확인
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "message", "Internal job API is running"
        ));
    }
}