package com.capstone.result.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse {

    @JsonProperty("result_id")
    private String resultId;

    @JsonProperty("tryon_id")
    private String tryonId;

    @JsonProperty("result_image_url")
    private String resultImageUrl;

    @JsonProperty("result_thumbnail_url")
    private String resultThumbnailUrl;

    @JsonProperty("generation_ms")
    private Integer generationMs;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
}