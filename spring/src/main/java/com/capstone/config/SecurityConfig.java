package com.capstone.config;

import com.capstone.security.handler.CustomAuthenticationEntryPoint;
import com.capstone.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ── 공개 경로 (인증 불필요) ───────────────────────────
                        .requestMatchers(
                                "/error",
                                "/api/v1/auth/register",   // 변경: /api/auth → /api/v1/auth
                                "/api/v1/auth/login",      // 변경: /api/auth → /api/v1/auth
                                "/api/v1/health",
                                "/api/v1/models/status"
                        ).permitAll()

                        // 의류 목록/검색/상세 비로그인 조회 허용
                        .requestMatchers(HttpMethod.GET, "/api/v1/garments/**").permitAll()  // 변경: /api/garments → /api/v1/garments

                        // 29CM 카테고리·브랜드·상품 조회 GET은 공개
                        .requestMatchers(HttpMethod.GET, "/api/v1/29cm/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/29cm/brands/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/29cm/catalog/items/**").permitAll()

                        // 내부 워커 API (Python worker 전용)
                        // 추후 Worker-Token 헤더 검증으로 교체 권장
                        .requestMatchers("/api/internal/**").permitAll()

                        // ── 관리자/판매자 전용 ────────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/api/v1/29cm/catalog/import/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/29cm/images/presign").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/garments/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/garments/**").hasAnyRole("ADMIN", "SELLER")

                        // ── 그 외 모든 요청 인증 필요 ─────────────────────────
                        // 제거: tryons/results permitAll 삭제 → 로그인 필수
                        // 제거: /api/search/garment, /api/recommendations (명세에서 통합/삭제)
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}