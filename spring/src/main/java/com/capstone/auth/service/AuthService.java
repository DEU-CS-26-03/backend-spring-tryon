package com.capstone.auth.service;

import com.capstone.auth.dto.LoginRequest;
import com.capstone.auth.dto.LoginResponse;
import com.capstone.auth.dto.RegisterRequest;
import com.capstone.auth.dto.RegisterResponse;
import com.capstone.security.jwt.JwtTokenProvider;
import com.capstone.user.entity.User;
import com.capstone.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        // @Valid가 기본 검증을 처리하므로, 비즈니스 중복 체크만 남김
        String email = normalizeEmail(request.getEmail());
        String nickname = request.getNickname().trim();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setNickname(nickname);
        // role, status 는 Entity 기본값(USER, ACTIVE) 사용

        User saved = userRepository.save(user);
        return RegisterResponse.builder()
                .message("회원가입이 완료되었습니다.")
                .user(RegisterResponse.UserSummary.builder()
                        .id(saved.getId())
                        .email(saved.getEmail())
                        .nickname(saved.getNickname())
                        .role(saved.getRole().name())
                        .build())
                .build();
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getEmail());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .user(LoginResponse.UserInfo.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .role(user.getRole().name())
                        .build())
                .build();
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}