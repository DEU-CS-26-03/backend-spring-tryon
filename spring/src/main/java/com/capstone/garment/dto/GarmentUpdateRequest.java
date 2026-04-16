package com.capstone.garment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GarmentUpdateRequest {
    private String category;
    private String brandKey;
    private String status;   // ACTIVE | HIDDEN
}