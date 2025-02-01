package com.example.imagehub.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageServiceFileTest {

    private final String uploadDir = "src/test/uploads";
    private final String thumbnailDir = "src/test/thumbnails";
    @InjectMocks
    private ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(imageService, "uploadDir", uploadDir);
        ReflectionTestUtils.setField(imageService, "thumbnailDir", thumbnailDir);
    }

    @Test
    void testSaveFileAndGenerateThumbnail() throws IOException {
        Path sampleImagePath = Path.of("src/test/resources/sample-image.jpg");
        Files.createDirectories(sampleImagePath.getParent());
        Files.copy(getClass().getResourceAsStream("/sample-image.jpg"), sampleImagePath, StandardCopyOption.REPLACE_EXISTING);

        MockMultipartFile file = new MockMultipartFile("file", "sample-image.jpg", "image/jpeg", Files.readAllBytes(sampleImagePath));
        String fileName = imageService.saveFile(file);
        String thumbnailPath = imageService.generateThumbnail(fileName);

        assertNotNull(fileName);
        assertNotNull(thumbnailPath);
        assertTrue(fileName.endsWith("sample-image.jpg"));
        assertTrue(thumbnailPath.contains("thumb_"));

        Files.deleteIfExists(Path.of(uploadDir, fileName));
        Files.deleteIfExists(Path.of(thumbnailPath));
    }
}
