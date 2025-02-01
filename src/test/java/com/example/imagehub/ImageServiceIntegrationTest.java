package com.example.imagehub;

import com.example.imagehub.application.service.ImageService;
import com.example.imagehub.domain.model.ImageModel;
import com.example.imagehub.infrastructure.config.AbstractSpringBootTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ImageServiceIntegrationTest extends AbstractSpringBootTest {

    @Autowired
    private ImageService imageService;

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        // 테스트용 임시 디렉토리 생성
        tempDir = Files.createTempDirectory("test-images");
    }

    @AfterEach
    void tearDown() throws IOException {
        // 테스트 후 임시 디렉토리 정리
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder()) // 디렉토리 삭제 전에 파일 먼저 삭제
                .map(Path::toFile)
                .forEach(file -> {
                    if (!file.delete()) {
                        System.err.println("Failed to delete file: " + file.getAbsolutePath());
                    }
                });
    }

    @Test
    void testUploadAndRetrieveImages() throws IOException {
        // 샘플 이미지 복사
        Path sampleImagePath = tempDir.resolve("sample-image.jpg");
        Files.copy(getClass().getResourceAsStream("/sample-image.jpg"), sampleImagePath);

        MockMultipartFile file = new MockMultipartFile("file", "sample-image.jpg", "image/jpeg", Files.readAllBytes(sampleImagePath));
        imageService.uploadImage(file, "Test Description", List.of("PERSON"));

        List<ImageModel> images = imageService.getImages();
        assertTrue(images.stream().anyMatch(img -> img.getFileName().contains("sample-image")));
    }

    @Test
    void testDeleteImage() throws IOException {
        // 샘플 이미지 복사
        Path sampleImagePath = tempDir.resolve("sample-image.jpg");
        Files.copy(getClass().getResourceAsStream("/sample-image.jpg"), sampleImagePath);

        MockMultipartFile file = new MockMultipartFile("file", "sample-image.jpg", "image/jpeg", Files.readAllBytes(sampleImagePath));
        imageService.uploadImage(file, "Delete Description", List.of("ANIMAL"));

        List<ImageModel> imagesBefore = imageService.getImages();
        Long imageId = imagesBefore.stream()
                .filter(img -> img.getFileName().contains("sample-image"))
                .findFirst()
                .map(ImageModel::getId)
                .orElseThrow();

        imageService.deleteImage(imageId);
        List<ImageModel> imagesAfter = imageService.getImages();
        assertFalse(imagesAfter.stream().anyMatch(img -> img.getId().equals(imageId)));
    }

    @Test
    void testGetImageById() throws IOException {
        // 샘플 이미지 복사
        Path sampleImagePath = tempDir.resolve("sample-image.jpg");
        Files.copy(getClass().getResourceAsStream("/sample-image.jpg"), sampleImagePath);

        MockMultipartFile file = new MockMultipartFile("file", "sample-image.jpg", "image/jpeg", Files.readAllBytes(sampleImagePath));
        imageService.uploadImage(file, "Single Image Description", List.of("FOOD"));

        List<ImageModel> images = imageService.getImages();
        Long imageId = images.stream()
                .filter(img -> img.getFileName().contains("sample-image"))
                .findFirst()
                .map(ImageModel::getId)
                .orElseThrow();

        ImageModel image = imageService.getImage(imageId);
        assertTrue(image.getFileName().contains("sample-image"));
        assertNotNull(image.getFilePath());
        assertNotNull(image.getThumbnailPath());
    }

    @Test
    void testGetImagesByCategory() throws IOException {
        // 샘플 이미지 복사
        Path sampleImagePath1 = tempDir.resolve("sample-image1.jpg");
        Path sampleImagePath2 = tempDir.resolve("sample-image2.jpg");
        Files.copy(getClass().getResourceAsStream("/sample-image.jpg"), sampleImagePath1);
        Files.copy(getClass().getResourceAsStream("/sample-image.jpg"), sampleImagePath2);

        MockMultipartFile file1 = new MockMultipartFile("file", "sample-image1.jpg", "image/jpeg", Files.readAllBytes(sampleImagePath1));
        MockMultipartFile file2 = new MockMultipartFile("file", "sample-image2.jpg", "image/jpeg", Files.readAllBytes(sampleImagePath2));
        imageService.uploadImage(file1, "Landscape 1", List.of("LANDSCAPE"));
        imageService.uploadImage(file2, "Landscape 2", List.of("LANDSCAPE"));

        List<ImageModel> images = imageService.getImages();
        List<ImageModel> filteredImages = images.stream()
                .filter(img -> img.getCategories().contains("LANDSCAPE"))
                .toList();

        assertTrue(2 < filteredImages.size());
    }
}
