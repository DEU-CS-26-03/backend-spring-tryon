//API 응답 포맷
package com.capstone.userimage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class UserImageResponse {

    @JsonProperty("image_id")
    private String imageId;

    private String status;
    private String view;
    private String filename;

    @JsonProperty("content_type")
    private String contentType;

    @JsonProperty("file_url")
    private String fileUrl;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    public UserImageResponse() {}

    public UserImageResponse(String imageId, String status, String view,
                             String filename, String contentType,
                             String fileUrl, OffsetDateTime createdAt) {
        this.imageId = imageId;
        this.status = status;
        this.view = view;
        this.filename = filename;
        this.contentType = contentType;
        this.fileUrl = fileUrl;
        this.createdAt = createdAt;
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