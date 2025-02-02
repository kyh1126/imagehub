package com.example.imagehub;

import com.example.imagehub.application.port.in.UploadImageCommand;
import com.example.imagehub.application.port.out.ImageResponse;
import com.example.imagehub.application.service.ImageService;
import com.example.imagehub.infrastructure.config.AbstractSpringBootTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Mock
    private Pageable pageRequest;

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
        UploadImageCommand uploadImageCommand = new UploadImageCommand(file, "Test Description", List.of("PERSON"));
        imageService.uploadImage(uploadImageCommand);

        var images = imageService.getImages(PageRequest.ofSize(10));
        assertTrue(images.stream().anyMatch(img -> img.getFileName().contains("sample-image")));
    }

    @Test
    void testDeleteImage() throws IOException {
        // 샘플 이미지 복사
        Path sampleImagePath = tempDir.resolve("sample-image.jpg");
        Files.copy(getClass().getResourceAsStream("/sample-image.jpg"), sampleImagePath);

        MockMultipartFile file = new MockMultipartFile("file", "sample-image.jpg", "image/jpeg", Files.readAllBytes(sampleImagePath));
        UploadImageCommand uploadImageCommand = new UploadImageCommand(file, "Delete Description", List.of("ANIMAL"));
        imageService.uploadImage(uploadImageCommand);

        var imagesBefore = imageService.getImages(PageRequest.ofSize(10));
        Long imageId = imagesBefore.stream()
                .filter(img -> img.getFileName().contains("sample-image"))
                .findFirst()
                .map(ImageResponse::getId)
                .orElseThrow();

        imageService.deleteImage(imageId);
        var imagesAfter = imageService.getImages(PageRequest.ofSize(10));
        assertFalse(imagesAfter.stream().anyMatch(img -> img.getId().equals(imageId)));
    }

    @Test
    void testGetImageById() throws IOException {
        // 샘플 이미지 복사
        Path sampleImagePath = tempDir.resolve("sample-image.jpg");
        Files.copy(getClass().getResourceAsStream("/sample-image.jpg"), sampleImagePath);

        MockMultipartFile file = new MockMultipartFile("file", "sample-image.jpg", "image/jpeg", Files.readAllBytes(sampleImagePath));
        UploadImageCommand uploadImageCommand = new UploadImageCommand(file, "Single Image Description", List.of("FOOD"));
        imageService.uploadImage(uploadImageCommand);

        var images = imageService.getImages(PageRequest.ofSize(10));
        Long imageId = images.stream()
                .filter(img -> img.getFileName().contains("sample-image"))
                .findFirst()
                .map(ImageResponse::getId)
                .orElseThrow();

        var image = imageService.getImage(imageId);
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
        UploadImageCommand uploadImageCommand1 = new UploadImageCommand(file1, "Landscape 1", List.of("LANDSCAPE"));
        UploadImageCommand uploadImageCommand2 = new UploadImageCommand(file2, "Landscape 2", List.of("LANDSCAPE"));
        imageService.uploadImage(uploadImageCommand1);
        imageService.uploadImage(uploadImageCommand2);

        var images = imageService.getImages(PageRequest.ofSize(10));
        var filteredImages = images.stream()
                .filter(img -> img.getCategories().contains("LANDSCAPE"))
                .toList();

        assertTrue(2 < filteredImages.size());
    }
}
