//POST /user-images, GET /user-images/{id}
package com.capstone.userimage.controller;

import com.capstone.userimage.dto.UserImageResponse;
import com.capstone.userimage.service.UserImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/user-images")
public class UserImageController {

    private final UserImageService service;

    public UserImageController(UserImageService service) {
        this.service = service;
    }

    /**
     * POST /api/v1/user-images
     * 사용자 전신 사진 업로드
     * - Content-Type: multipart/form-data
     * - file: 이미지 파일 (jpg, png만 허용)
     * - view: 촬영 방향 (현재 front만 허용, 기본값 front)
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<UserImageResponse> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "view", defaultValue = "front") String view
    ) throws IOException {
        UserImageResponse response = service.upload(file, view);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/user-images/{id}
     * 업로드된 사용자 사진 메타정보 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserImageResponse> getById(
            @PathVariable("id") String imageId
    ) {
        return ResponseEntity.ok(service.getById(imageId));
    }
}