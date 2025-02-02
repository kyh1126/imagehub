package com.example.imagehub.adapter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "이미지 업로드 요청 정보")
@Getter
@NoArgsConstructor
public class UploadImageRequest {

    @Schema(description = "설명")
    private String description;

    @Schema(description = "카테고리")
    private List<String> categories;

}
