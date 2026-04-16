package com.capstone.web_api.cm29.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cm29CategoryResponse {
    private String code;
    private String name;
    private Integer depth;

    @JsonProperty("parent_code")
    private String parentCode;

    @JsonProperty("is_leaf")
    private Boolean isLeaf;

    @JsonProperty("model_size_required")
    private Boolean modelSizeRequired;

    @JsonProperty("size_chart_available")
    private Boolean sizeChartAvailable;

    private List<Cm29CategoryResponse> children;
}