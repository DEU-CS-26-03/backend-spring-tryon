package com.capstone.garment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "garments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garment {

    @Id
    @Column(name = "garment_id")
    private String garmentId;

    @Column(name = "owner_user_id")
    private Long ownerUserId;

    @Column(name = "source_type")
    private String sourceType;

    @Column(name = "external_item_key")
    private String externalItemKey;

    @Column(name = "partner_brand_key")
    private String partnerBrandKey;

    @Column(name = "standard_category_code")
    private String standardCategoryCode;

    private String category;
    private String name;

    @Column(name = "brand_name")
    private String brandName;

    private BigDecimal price;
    private String currency;

    @Column(name = "file_url")
    private String fileUrl;          // ← getFileUrl() 여기서 나옴

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    private String status;

    @Builder.Default
    private boolean deleted = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}