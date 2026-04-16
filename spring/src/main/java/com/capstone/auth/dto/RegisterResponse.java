package com.capstone.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegisterResponse {
    private String message;
    private UserSummary user;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserSummary {
        private Long id;
        private String email;
        private String nickname;
        private String role;
    }
}