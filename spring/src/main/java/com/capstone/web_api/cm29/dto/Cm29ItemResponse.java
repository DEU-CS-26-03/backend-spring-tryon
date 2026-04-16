package com.capstone.cm29.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cm29ItemResponse {

    @JsonProperty("item_key")
    private String itemKey;

    @JsonProperty("item_name")
    private String itemName;

    @JsonProperty("partner_brand_key")
    private String partnerBrandKey;

    @JsonProperty("brand_name")
    private String brandName;

    @JsonProperty("sale_price")
    private BigDecimal salePrice;

    @JsonProperty("list_price")
    private BigDecimal listPrice;

    private String currency;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("standard_category_code")
    private String standardCategoryCode;

    private String status;
}