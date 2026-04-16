package com.capstone.internal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalJobStatusRequest {

    // queued | processing | completed | failed
    private String status;

    // 0 ~ 100
    private int progress;

    @JsonProperty("result_id")
    private String resultId;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("error_message")
    private String errorMessage;
}