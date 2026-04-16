package com.capstone.internal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InternalJobResponse {

    @JsonProperty("tryon_id")
    private String tryonId;

    @JsonProperty("user_image_path")
    private String userImagePath;

    @JsonProperty("garment_path")
    private String garmentPath;

    private String status;
}