package com.capstone.tryon.controller;

import com.capstone.tryon.dto.TryonCreateRequest;
import com.capstone.tryon.dto.TryonResponse;
import com.capstone.tryon.service.TryonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tryons")
@RequiredArgsConstructor
public class TryonController {

    private final TryonService tryonService;

    // POST /api/v1/tryons — 가상 피팅 작업 생성
    @PostMapping
    public ResponseEntity<TryonResponse> create(
            @Valid @RequestBody TryonCreateRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(tryonService.create(request, email));
    }

    // GET /api/v1/tryons — 내 작업 목록 조회
    @GetMapping
    public ResponseEntity<List<TryonResponse>> list(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(tryonService.listByUser(email));
    }

    // GET /api/v1/tryons/{tryonId} — 작업 상태 조회 (polling)
    @GetMapping("/{tryonId}")
    public ResponseEntity<TryonResponse> getById(@PathVariable String tryonId) {
        return ResponseEntity.ok(tryonService.getById(tryonId));
    }

    // DELETE /api/v1/tryons/{tryonId} — 작업 비노출 처리
    @DeleteMapping("/{tryonId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String tryonId) {
        tryonService.softDelete(tryonId);
        return ResponseEntity.ok(Map.of("message", "작업이 삭제되었습니다."));
    }
}