package com.capstone.tryon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tryon_jobs")
@Getter
@Setter
public class TryonJob {

    @Id
    @Column(name = "tryon_id", length = 50)
    private String tryonId;

    @Column(name = "user_id")
    private Long userId;

    // queued | processing | completed | failed | cancelled
    @Column(nullable = false, length = 20)
    private String status = "queued";

    @Column(nullable = false)
    private int progress = 0;

    @Column(name = "user_image_id", nullable = false)
    private String userImageId;

    // garmentId와 externalItemKey 중 하나만 필수
    @Column(name = "garment_id")
    private String garmentId;

    @Column(name = "external_item_key")
    private String externalItemKey;

    @Column(name = "result_id")
    private String resultId;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(nullable = false)
    private boolean deleted = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}