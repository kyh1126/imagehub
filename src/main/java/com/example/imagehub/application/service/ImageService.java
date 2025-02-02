package com.example.imagehub.application.service;

import com.example.imagehub.application.port.in.ImageUseCase;
import com.example.imagehub.application.port.in.UploadImageCommand;
import com.example.imagehub.application.port.out.ImageResponse;
import com.example.imagehub.application.port.out.LoadImagePort;
import com.example.imagehub.application.port.out.UpdateImagePort;
import com.example.imagehub.application.port.out.UploadImagePort;
import com.example.imagehub.common.UseCase;
import com.example.imagehub.domain.Image;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RequiredArgsConstructor
@UseCase
@Transactional(readOnly = true)
public class ImageService implements ImageUseCase {

    private static final Set<String> validCategories = Set.of("PERSON", "LANDSCAPE", "ANIMAL", "FOOD", "OTHERS");
    private final UploadImagePort uploadImagePort;
    private final LoadImagePort loadImagePort;
    private final UpdateImagePort updateImagePort;
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.thumbnail.dir}")
    private String thumbnailDir;

    @Transactional
    @Override
    public void uploadImage(UploadImageCommand uploadImageCommand) {
        validateCategories(uploadImageCommand.getCategories());

        String fileName = saveFile(uploadImageCommand.getFile());
        String thumbnailPath = generateThumbnail(fileName);

        Image image = Image.of(uploadImageCommand, fileName, uploadDir, thumbnailPath);

        uploadImagePort.upload(image);
    }

    @Override
    public List<ImageResponse> getImages(Pageable pageable) {
        return loadImagePort.getImages(pageable).stream()
                .map(ImageResponse::from)
                .toList();
    }

    @Override
    public ImageResponse getImage(Long id) {
        Image image = loadImagePort.getImage(id);
        return ImageResponse.from(image);
    }

    @Override
    public List<String> getImageCategories(Long id) {
        return loadImagePort.findCategoriesById(id);
    }

    @Transactional
    @Override
    public void deleteImage(Long id) {
        Image image = loadImagePort.getImage(id);
        deleteFile(image.getFilePath());  // 원본 이미지 삭제
        deleteFile(image.getThumbnailPath()); // 썸네일 삭제
        updateImagePort.deleteById(id);
    }

    @Transactional
    @Override
    public void addCategoriesToImage(Long id, List<String> categories) {
        validateCategories(categories);
        Image image = loadImagePort.getImage(id);
        image.getCategories().addAll(categories);
        updateImagePort.update(image);
    }

    @Transactional
    @Override
    public void removeCategoriesFromImage(Long id, List<String> categories) {
        validateCategories(categories);
        Image image = loadImagePort.getImage(id);
        image.getCategories().removeAll(categories);
        updateImagePort.update(image);
    }

    private void validateCategories(List<String> categories) {
        for (String category : categories) {
            if (!validCategories.contains(category)) {
                throw new IllegalArgumentException("Invalid category: " + category);
            }
        }
    }

    String saveFile(MultipartFile file) {
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        checkImageFormat(originalFilename);

        try {
            Files.createDirectories(Path.of(uploadDir));

            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Path.of(uploadDir, uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 파일 크기 확인
            if (Files.size(filePath) == 0) {
                throw new IOException("File is empty after saving: " + filePath);
            }

            return uniqueFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    String generateThumbnail(String fileName) {
        try {
            Files.createDirectories(Path.of(thumbnailDir));

            String thumbnailName = "thumb_" + fileName;
            Path inputPath = Path.of(uploadDir, fileName);
            Path outputPath = Path.of(thumbnailDir, thumbnailName);

            Thumbnails.of(ImageIO.read(inputPath.toFile()))
                    .size(100, 100)
                    .toFile(outputPath.toFile());

            return thumbnailName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate thumbnail", e);
        }
    }

    private void checkImageFormat(String fileName) {
        String[] formatNames = ImageIO.getReaderFormatNames();

        boolean isSupported = Arrays.stream(formatNames)
                .map(String::toLowerCase)
                .anyMatch(format -> fileName.toLowerCase().endsWith("." + format));

        if (!isSupported) {
            throw new RuntimeException("Unsupported image format: " + fileName);
        }
    }

    void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Path.of(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}
