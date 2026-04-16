package com.capstone.web_api.cm29.controller;

import com.capstone.cm29.dto.*;
import com.capstone.cm29.service.Cm29Service;
import com.capstone.garment.dto.GarmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/29cm")
@RequiredArgsConstructor
public class Cm29Controller {

    private final Cm29Service service;

    // GET /api/v1/29cm/categories/standard — 표준 카테고리 트리 조회
    @GetMapping("/categories/standard")
    public ResponseEntity<List<Cm29CategoryResponse>> getStandardCategories() {
        return ResponseEntity.ok(service.getStandardCategories());
    }

    // GET /api/v1/29cm/categories/display — 전시 카테고리 목록 조회
    @GetMapping("/categories/display")
    public ResponseEntity<List<Cm29CategoryResponse>> getDisplayCategories() {
        return ResponseEntity.ok(service.getDisplayCategories());
    }

    // GET /api/v1/29cm/categories/{code} — 카테고리 상세 조회
    @GetMapping("/categories/{code}")
    public ResponseEntity<Cm29CategoryResponse> getCategoryByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getCategoryByCode(code));
    }

    // GET /api/v1/29cm/brands — 브랜드 리스트 조회
    @GetMapping("/brands")
    public ResponseEntity<List<Cm29BrandResponse>> getBrands() {
        return ResponseEntity.ok(service.getBrands());
    }

    // GET /api/v1/29cm/brands/{partnerBrandKey} — 브랜드 상세 조회
    @GetMapping("/brands/{partnerBrandKey}")
    public ResponseEntity<Cm29BrandResponse> getBrandByKey(@PathVariable String partnerBrandKey) {
        return ResponseEntity.ok(service.getBrandByKey(partnerBrandKey));
    }

    // POST /api/v1/29cm/catalog/search — 상품 검색
    @PostMapping("/catalog/search")
    public ResponseEntity<List<Cm29ItemResponse>> searchCatalog(
            @RequestBody Cm29CatalogSearchRequest request
    ) {
        return ResponseEntity.ok(service.searchCatalog(request));
    }

    // GET /api/v1/29cm/catalog/items/{itemKey} — 상품 상세 조회
    @GetMapping("/catalog/items/{itemKey}")
    public ResponseEntity<Cm29ItemResponse> getCatalogItem(@PathVariable String itemKey) {
        return ResponseEntity.ok(service.getCatalogItem(itemKey));
    }

    // POST /api/v1/29cm/catalog/import/{itemKey} — 29CM 상품을 내부 garment로 저장
    @PostMapping("/catalog/import/{itemKey}")
    public ResponseEntity<GarmentResponse> importItem(@PathVariable String itemKey) {
        return ResponseEntity.ok(service.importItem(itemKey));
    }

    // POST /api/v1/29cm/images/presign — 이미지 업로드 URL 생성
    @PostMapping("/images/presign")
    public ResponseEntity<Map<String, String>> generatePresignedUrl(
            @RequestBody Map<String, String> body
    ) {
        String url = service.generatePresignedUrl(body.get("filename"));
        return ResponseEntity.ok(Map.of("presigned_url", url));
    }
}