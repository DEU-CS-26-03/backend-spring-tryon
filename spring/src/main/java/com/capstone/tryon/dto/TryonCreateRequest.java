package com.capstone.tryon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TryonCreateRequest {

    @JsonProperty("user_image_id")
    @NotBlank(message = "user_image_id는 필수입니다.")
    private String userImageId;

    // garmentId 또는 externalItemKey 중 하나 필수 (서비스에서 검증)
    @JsonProperty("garment_id")
    private String garmentId;

    @JsonProperty("external_item_key")
    private String externalItemKey;
}