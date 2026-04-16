package com.capstone.result.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "results")
@Getter
@Setter
public class Result {

    @Id
    @Column(name = "result_id", length = 50)
    private String resultId;

    // 어떤 tryon 작업의 결과인지
    @Column(name = "tryon_id", nullable = false, unique = true)
    private String tryonId;

    // 결과를 소유한 사용자 (결과 목록 조회 시 필요)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // COMPLETED | FAILED | DELETED
    @Column(nullable = false, length = 20)
    private String status = "COMPLETED";

    // 결과 이미지 URL (기존 result_url)
    @Column(name = "result_image_url")
    private String resultImageUrl;

    @Column(name = "result_thumbnail_url")
    private String resultThumbnailUrl;

    // Python worker 처리 시간 (ms)
    @Column(name = "generation_ms")
    private Integer generationMs;

    // ── 피드백 필드 ─────────────────────────────────────────

    // 별점 1~5
    @Column(name = "rating")
    private Integer rating;

    // 코멘트 (선택)
    @Column(name = "comment", length = 1000)
    private String comment;

    // SIMILAR | CONTRAST | MIXED
    @Column(name = "recommendation_mode", length = 20)
    private String recommendationMode;

    // 추천 기준 카테고리 (피팅 당시 의류 카테고리)
    @Column(name = "garment_category", length = 50)
    private String garmentCategory;

    // ── soft delete ──────────────────────────────────────────

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
}