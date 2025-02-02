package com.example.imagehub.adapter.in;

import com.example.imagehub.adapter.dto.UploadImageRequest;
import com.example.imagehub.application.port.in.ImageUseCase;
import com.example.imagehub.application.port.in.UploadImageCommand;
import com.example.imagehub.application.port.out.ImageResponse;
import com.example.imagehub.common.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "Image", description = "이미지 관리 API")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("images")
@SecurityRequirement(name = "Bearer Authentication")
public class ImageController {

    private final ImageUseCase imageUseCase;

    @Operation(summary = "이미지 업로드")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadImage(@RequestPart("file") MultipartFile file,
                                           @RequestPart("request") @Valid UploadImageRequest request) {
        UploadImageCommand uploadImageCommand = new UploadImageCommand(file, request.getDescription(), request.getCategories());

        imageUseCase.uploadImage(uploadImageCommand);

        return Map.of("message", "Image uploaded successfully");
    }

    @Operation(summary = "저장된 이미지 목록 조회")
    @GetMapping
    public List<ImageResponse> getImages(@PageableDefault Pageable pageable) {
        return imageUseCase.getImages(pageable);
    }

    @Operation(summary = "특정 이미지 조회")
    @GetMapping("{id}")
    public ImageResponse getImage(@PathVariable Long id) {
        return imageUseCase.getImage(id);
    }

    @Operation(summary = "특정 이미지의 카테고리 조회")
    @GetMapping("/{id}/categories")
    public List<String> getImageCategories(@PathVariable Long id) {
        return imageUseCase.getImageCategories(id);
    }

    @Operation(summary = "저장된 이미지 삭제")
    @DeleteMapping("{id}")
    public Map<String, String> deleteImage(@PathVariable Long id) {
        imageUseCase.deleteImage(id);
        return Map.of("message", "Image deleted successfully");
    }

    @Operation(summary = "이미지의 카테고리 추가")
    @PostMapping("/{id}/categories")
    public Map<String, String> addCategoriesToImage(@PathVariable Long id, @RequestBody List<String> categories) {
        imageUseCase.addCategoriesToImage(id, categories);
        return Map.of("message", "Categories added successfully");
    }

    @Operation(summary = "이미지의 카테고리 삭제")
    @DeleteMapping("/{id}/categories")
    public Map<String, String> removeCategoriesFromImage(@PathVariable Long id, @RequestBody List<String> categories) {
        imageUseCase.removeCategoriesFromImage(id, categories);
        return Map.of("message", "Categories removed successfully");
    }
}
