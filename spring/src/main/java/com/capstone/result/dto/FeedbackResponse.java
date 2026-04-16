package com.capstone.result.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedbackResponse {
    private Integer rating;

    @JsonProperty("recommendation_mode")
    private String recommendationMode;  // SIMILAR | CONTRAST | MIXED

    private String message;
}