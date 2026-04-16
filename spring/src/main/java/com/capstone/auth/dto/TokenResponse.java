package com.capstone.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 토큰 값만 담는 단순 VO — LoginResponse 조립 시 사용
@Getter
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String tokenType;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
    }
}