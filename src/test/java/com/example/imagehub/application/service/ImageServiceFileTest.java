package com.example.imagehub.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

class ImageServiceFileTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private Path mockPath;

    //    @Value("${app.upload.dir}")
    private String uploadDir = "src/test/uploads";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(imageService, "uploadDir", uploadDir);
    }

    @Test
    void testSaveFile() throws IOException {
        Path sampleImagePath = Path.of("src/test/resources/sample-image.jpg");
        Files.createDirectories(sampleImagePath.getParent());
        Files.copy(getClass().getResourceAsStream("/sample-image.jpg"), sampleImagePath, StandardCopyOption.REPLACE_EXISTING);

        MockMultipartFile file = new MockMultipartFile("file", "sample-image.jpg", "image/jpeg", Files.readAllBytes(sampleImagePath));
        String fileName = imageService.saveFile(file);
        assertNotNull(fileName);
        assertTrue(fileName.endsWith("sample-image.jpg"));
        Files.deleteIfExists(Path.of(uploadDir, fileName));
    }

    @Test
    void testDeleteFile() throws IOException {
        Path sampleImagePath = Path.of("src/test/resources/sample-image.jpg");
        Files.createDirectories(sampleImagePath.getParent());
        Files.copy(getClass().getResourceAsStream("/sample-image.jpg"), sampleImagePath, StandardCopyOption.REPLACE_EXISTING);

        MockMultipartFile file = new MockMultipartFile("file", "sample-image.jpg", "image/jpeg", Files.readAllBytes(sampleImagePath));
        String fileName = imageService.saveFile(file);
        imageService.deleteFile(fileName);
        assertFalse(Files.exists(Path.of(uploadDir, fileName)));
    }
}
