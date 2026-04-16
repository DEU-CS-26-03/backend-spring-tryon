package com.capstone.web_api.cm29.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cm29CatalogSearchRequest {

    @JsonProperty("item_name")
    private String itemName;

    @JsonProperty("brand_key")
    private String brandKey;

    private Integer page;
    private Integer size;
}