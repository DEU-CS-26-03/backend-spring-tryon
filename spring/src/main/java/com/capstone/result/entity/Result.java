package com.capstone.result.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tryon_id")
    private String tryonId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "result_image_url")
    private String resultImageUrl;

    @Column(name = "result_thumbnail_url")
    private String resultThumbnailUrl;

    private int rating;
    private String comment;

    @Column(name = "recommendation_mode")
    private String recommendationMode;

    @Builder.Default
    private boolean deleted = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
}