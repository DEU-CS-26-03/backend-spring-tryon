package com.capstone.tryon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TryonErrorInfo {
    private String code;
    private String message;
}