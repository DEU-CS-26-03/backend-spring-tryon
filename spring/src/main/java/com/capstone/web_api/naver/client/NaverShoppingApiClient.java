package com.capstone.web_api.naver.client;

import com.capstone.web_api.cm29.dto.Cm29ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NaverShoppingApiClient {

    private final WebClient naverWebClient;

    @SuppressWarnings("unchecked")
    public List<Cm29ItemResponse> searchItems(String query, int display) {
        Map<String, Object> response = naverWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/shop.json")
                        .queryParam("query", query)
                        .queryParam("display", display)
                        .queryParam("sort", "sim")
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        resp -> resp.bodyToMono(String.class)
                                .map(body -> new IllegalArgumentException(
                                        "네이버 API 요청 오류: " + body)))
                .onStatus(status -> status.is5xxServerError(),
                        resp -> resp.bodyToMono(String.class)
                                .map(body -> new RuntimeException(
                                        "네이버 서버 오류: " + body)))
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("items")) {
            return List.of();
        }

        List<Map<String, Object>> items =
                (List<Map<String, Object>>) response.get("items");

        return items.stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());
    }

    private Cm29ItemResponse toItemResponse(Map<String, Object> item) {
        // <b>태그</b> 제거
        String title = item.getOrDefault("title", "").toString()
                .replaceAll("<[^>]*>", "");

        String lprice = item.getOrDefault("lprice", "0").toString();
        String hprice = item.getOrDefault("hprice", "0").toString();

        // category3이 가장 구체적 → 없으면 category2
        String category = item.getOrDefault("category3", "").toString();
        if (category.isBlank()) {
            category = item.getOrDefault("category2", "").toString();
        }

        String brandName = item.getOrDefault("brand", "").toString();
        if (brandName.isBlank()) {
            brandName = item.getOrDefault("mallName", "").toString();
        }

        return new Cm29ItemResponse(
                item.getOrDefault("productId", "").toString(),
                title,
                null,
                brandName,
                parsePrice(lprice),
                parsePrice(hprice),
                "KRW",
                item.getOrDefault("image", "").toString(),
                category,
                "ACTIVE"
        );
    }

    private BigDecimal parsePrice(String price) {
        if (price == null || price.isBlank() || "0".equals(price)) return null;
        try {
            return new BigDecimal(price);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}