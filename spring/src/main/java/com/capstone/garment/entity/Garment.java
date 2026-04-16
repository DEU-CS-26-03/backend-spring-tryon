package com.capstone.garment.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "garments")
public class Garment {

    @Id
    @Column(name = "garment_id", length = 50)
    private String garmentId;

    @Column(name = "owner_user_id")
    private Long ownerUserId;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";          // ACTIVE | HIDDEN | DELETED

    @Column(name = "source_type", length = 30)
    private String sourceType = "UPLOAD";      // UPLOAD | 29CM_IMPORT

    @Column(length = 30)
    private String category;

    @Column(nullable = false)
    private String filename;

    @Column(name = "content_type", length = 50)
    private String contentType;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "brand_key")
    private String brandKey;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }

    public String getGarmentId()              { return garmentId; }
    public void setGarmentId(String v)        { this.garmentId = v; }
    public Long getOwnerUserId()              { return ownerUserId; }
    public void setOwnerUserId(Long v)        { this.ownerUserId = v; }
    public String getStatus()                 { return status; }
    public void setStatus(String v)           { this.status = v; }
    public String getSourceType()             { return sourceType; }
    public void setSourceType(String v)       { this.sourceType = v; }
    public String getCategory()               { return category; }
    public void setCategory(String v)         { this.category = v; }
    public String getFilename()               { return filename; }
    public void setFilename(String v)         { this.filename = v; }
    public String getContentType()            { return contentType; }
    public void setContentType(String v)      { this.contentType = v; }
    public String getFileUrl()                { return fileUrl; }
    public void setFileUrl(String v)          { this.fileUrl = v; }
    public String getBrandKey()               { return brandKey; }
    public void setBrandKey(String v)         { this.brandKey = v; }
    public OffsetDateTime getCreatedAt()      { return createdAt; }
    public void setCreatedAt(OffsetDateTime v){ this.createdAt = v; }
}