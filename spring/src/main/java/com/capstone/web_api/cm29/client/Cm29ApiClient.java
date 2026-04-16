package com.capstone.web_api.cm29.client;

import com.capstone.cm29.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Cm29ApiClient {

    private final WebClient cm29WebClient;

    // ── 카테고리 ──────────────────────────────────────────────

    public List<Cm29CategoryResponse> getStandardCategories() {
        return cm29WebClient.get()
                .uri("/categories/standard")
                .retrieve()
                .bodyToFlux(Cm29CategoryResponse.class)
                .collectList()
                .block();
    }

    public List<Cm29CategoryResponse> getDisplayCategories() {
        return cm29WebClient.get()
                .uri("/categories/display")
                .retrieve()
                .bodyToFlux(Cm29CategoryResponse.class)
                .collectList()
                .block();
    }

    public Cm29CategoryResponse getCategoryByCode(String code) {
        return cm29WebClient.get()
                .uri("/categories/{code}", code)
                .retrieve()
                .bodyToMono(Cm29CategoryResponse.class)
                .block();
    }

    // ── 브랜드 ──────────────────────────────────────────────

    public List<Cm29BrandResponse> getBrands() {
        return cm29WebClient.get()
                .uri("/brands")
                .retrieve()
                .bodyToFlux(Cm29BrandResponse.class)
                .collectList()
                .block();
    }

    public Cm29BrandResponse getBrandByKey(String partnerBrandKey) {
        return cm29WebClient.get()
                .uri("/brands/{partnerBrandKey}", partnerBrandKey)
                .retrieve()
                .bodyToMono(Cm29BrandResponse.class)
                .block();
    }

    // ── 상품 ─────────────────────────────────────────────────

    public List<Cm29ItemResponse> searchCatalog(Cm29CatalogSearchRequest request) {
        return cm29WebClient.post()
                .uri("/catalog/search")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(Cm29ItemResponse.class)
                .collectList()
                .block();
    }

    public Cm29ItemResponse getItem(String itemKey) {
        return cm29WebClient.get()
                .uri("/catalog/items/{itemKey}", itemKey)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        resp -> resp.bodyToMono(String.class)
                                .map(body -> new IllegalArgumentException(
                                        "29CM 상품 없음: " + itemKey)))
                .onStatus(status -> status.is5xxServerError(),
                        resp -> resp.bodyToMono(String.class)
                                .map(body -> new RuntimeException(
                                        "29CM 서버 오류: " + body)))
                .bodyToMono(Cm29ItemResponse.class)
                .block();
    }

    // ── 이미지 presign URL ────────────────────────────────────

    public String generatePresignedUrl(String filename) {
        Map<?, ?> response = cm29WebClient.post()
                .uri("/images/presign")
                .bodyValue(Map.of("filename", filename))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return response != null ? (String) response.get("url") : null;
    }
}