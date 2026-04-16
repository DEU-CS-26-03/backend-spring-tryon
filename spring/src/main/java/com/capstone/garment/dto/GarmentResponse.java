package com.capstone.garment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class GarmentResponse {

    @JsonProperty("garment_id")
    private String garmentId;

    private String status;

    @JsonProperty("source_type")
    private String sourceType;       // UPLOAD | 29CM_IMPORT

    private String category;
    private String filename;

    @JsonProperty("content_type")
    private String contentType;

    @JsonProperty("file_url")
    private String fileUrl;

    @JsonProperty("brand_key")
    private String brandKey;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    public GarmentResponse() {}

    public GarmentResponse(String garmentId, String status, String sourceType,
                           String category, String filename, String contentType,
                           String fileUrl, String brandKey, OffsetDateTime createdAt) {
        this.garmentId   = garmentId;
        this.status      = status;
        this.sourceType  = sourceType;
        this.category    = category;
        this.filename    = filename;
        this.contentType = contentType;
        this.fileUrl     = fileUrl;
        this.brandKey    = brandKey;
        this.createdAt   = createdAt;
    }

    public String getGarmentId()              { return garmentId; }
    public void setGarmentId(String v)        { this.garmentId = v; }
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