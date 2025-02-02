package com.example.imagehub.application.port.in;

import com.example.imagehub.common.SelfValidating;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = false)
public class UploadImageCommand extends SelfValidating<UploadImageCommand> {
    @NotNull(message = "이미지 파일은 필수입니다.")
    MultipartFile file;
    String description;
    List<String> categories;

    public UploadImageCommand(MultipartFile file, String description, List<String> categories) {
        this.file = file;
        this.description = description;
        this.categories = categories;
        this.validateSelf();
    }
}

