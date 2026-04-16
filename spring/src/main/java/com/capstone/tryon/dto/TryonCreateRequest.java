package com.capstone.tryon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TryonCreateRequest {

    @NotBlank(message = "userImageId는 필수입니다.")
    @JsonProperty("user_image_id")
    private String userImageId;

    @JsonProperty("garment_id")
    private String garmentId;

    @JsonProperty("external_item_key")
    private String externalItemKey;
}