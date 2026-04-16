package com.capstone.tryon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class TryonResponse {

    @JsonProperty("tryon_id")
    private String tryonId;

    private String status;    // queued | processing | completed | failed

    private int progress;     // 0 ~ 100

    @JsonProperty("user_image_id")
    private String userImageId;

    @JsonProperty("garment_id")
    private String garmentId;

    @JsonProperty("result_id")
    private String resultId;

    private String message;

    private TryonErrorInfo error;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
}