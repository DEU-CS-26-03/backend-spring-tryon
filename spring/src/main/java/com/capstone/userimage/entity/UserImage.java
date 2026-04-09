//DB 테이블 매핑 (user_images)
package com.capstone.userimage.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_images")
public class UserImage {

    @Id
    @Column(name = "image_id", length = 50)
    private String imageId;

    @Column(nullable = false, length = 20)
    private String status = "uploaded";

    @Column(length = 20)
    private String view;

    @Column(nullable = false)
    private String filename;

    @Column(name = "content_type", length = 50)
    private String contentType;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }

    public String getImageId() { return imageId; }
    public void setImageId(String imageId) { this.imageId = imageId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getView() { return view; }
    public void setView(String view) { this.view = view; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}