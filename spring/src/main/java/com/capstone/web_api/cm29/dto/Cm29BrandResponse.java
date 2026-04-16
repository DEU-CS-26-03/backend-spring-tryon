package com.capstone.cm29.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cm29BrandResponse {

    @JsonProperty("partner_brand_key")
    private String partnerBrandKey;

    @JsonProperty("brand_name")
    private String brandName;

    @JsonProperty("brand_name_ko")
    private String brandNameKo;

    @JsonProperty("logo_url")
    private String logoUrl;

    private String status;
}