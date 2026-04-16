package com.capstone.cm29.service;

import com.capstone.cm29.client.Cm29ApiClient;
import com.capstone.cm29.dto.*;
import com.capstone.garment.dto.GarmentResponse;
import com.capstone.garment.entity.Garment;
import com.capstone.garment.repository.GarmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Cm29Service {

    private final Cm29ApiClient cm29ApiClient;
    private final GarmentRepository garmentRepository;

    // ── 카테고리 ──────────────────────────────────────────────

    public List<Cm29CategoryResponse> getStandardCategories() {
        return cm29ApiClient.getStandardCategories();
    }

    public List<Cm29CategoryResponse> getDisplayCategories() {
        return cm29ApiClient.getDisplayCategories();
    }

    public Cm29CategoryResponse getCategoryByCode(String code) {
        return cm29ApiClient.getCategoryByCode(code);
    }

    // ── 브랜드 ──────────────────────────────────────────────

    public List<Cm29BrandResponse> getBrands() {
        return cm29ApiClient.getBrands();
    }

    public Cm29BrandResponse getBrandByKey(String partnerBrandKey) {
        return cm29ApiClient.getBrandByKey(partnerBrandKey);
    }

    // ── 상품 ─────────────────────────────────────────────────

    public List<Cm29ItemResponse> searchCatalog(Cm29CatalogSearchRequest request) {
        return cm29ApiClient.searchCatalog(request);
    }

    public Cm29ItemResponse getCatalogItem(String itemKey) {
        return cm29ApiClient.getItem(itemKey);
    }

    @Transactional
    public GarmentResponse importItem(String itemKey) {
        // 1. 29CM에서 상품 정보 조회
        Cm29ItemResponse item = cm29ApiClient.getItem(itemKey);

        // 2. 이미 임포트된 상품이면 기존 것 반환
        garmentRepository.findByExternalItemKey(itemKey).ifPresent(existing -> {
            throw new IllegalStateException("이미 임포트된 상품입니다: " + itemKey);
        });

        // 3. 내부 Garment 엔티티로 변환 후 저장
        Garment garment = new Garment();
        garment.setGarmentId("gar_29cm_" + itemKey);
        garment.setSourceType("29CM_IMPORT");
        garment.setExternalItemKey(item.getItemKey());
        garment.setPartnerBrandKey(item.getPartnerBrandKey());
        garment.setBrandName(item.getBrandName());
        garment.setStandardCategoryCode(item.getStandardCategoryCode());
        garment.setName(item.getItemName());
        garment.setFileUrl(item.getThumbnailUrl());
        garment.setThumbnailUrl(item.getThumbnailUrl());
        garment.setPrice(item.getSalePrice());
        garment.setCurrency(item.getCurrency() != null ? item.getCurrency() : "KRW");
        garment.setStatus("ACTIVE");

        Garment saved = garmentRepository.save(garment);

        return new GarmentResponse(
                saved.getGarmentId(),
                saved.getStatus(),
                saved.getSourceType(),
                saved.getCategory(),
                saved.getName(),
                null,
                saved.getFileUrl(),
                saved.getPartnerBrandKey(),
                saved.getCreatedAt()
        );
    }

    // ── 이미지 presign URL ────────────────────────────────────

    public String generatePresignedUrl(String filename) {
        return cm29ApiClient.generatePresignedUrl(filename);
    }
}