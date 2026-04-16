package com.capstone.tryon.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "tryon_jobs")
public class TryonJob {

    @Id
    @Column(name = "tryon_id", length = 50)
    private String tryonId;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 20)
    private String status = "queued";

    @Column(nullable = false)
    private int progress = 0;

    @Column(name = "user_image_id", nullable = false)
    private String userImageId;

    @Column(name = "garment_id")          // nullable — externalItemKey와 선택적
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

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public String getTryonId()                       { return tryonId; }
    public void setTryonId(String tryonId)           { this.tryonId = tryonId; }
    public Long getUserId()                          { return userId; }
    public void setUserId(Long userId)               { this.userId = userId; }
    public String getStatus()                        { return status; }
    public void setStatus(String status)             { this.status = status; }
    public int getProgress()                         { return progress; }
    public void setProgress(int progress)            { this.progress = progress; }
    public String getUserImageId()                   { return userImageId; }
    public void setUserImageId(String userImageId)   { this.userImageId = userImageId; }
    public String getGarmentId()                     { return garmentId; }
    public void setGarmentId(String garmentId)       { this.garmentId = garmentId; }
    public String getExternalItemKey()               { return externalItemKey; }
    public void setExternalItemKey(String key)       { this.externalItemKey = key; }
    public String getResultId()                      { return resultId; }
    public void setResultId(String resultId)         { this.resultId = resultId; }
    public String getErrorCode()                     { return errorCode; }
    public void setErrorCode(String errorCode)       { this.errorCode = errorCode; }
    public String getErrorMessage()                  { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public boolean isDeleted()                       { return deleted; }
    public void setDeleted(boolean deleted)          { this.deleted = deleted; }
    public OffsetDateTime getCreatedAt()             { return createdAt; }
    public OffsetDateTime getUpdatedAt()             { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime v)       { this.updatedAt = v; }
}