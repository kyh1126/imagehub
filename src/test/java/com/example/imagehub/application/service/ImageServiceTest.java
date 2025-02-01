package com.example.imagehub.application.service;

import com.example.imagehub.application.port.out.ImagePort;
import com.example.imagehub.domain.model.ImageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    private final String uploadDir = "src/test/uploads";
    private final String thumbnailDir = "src/test/thumbnails";
    @Spy
    @InjectMocks
    private ImageService imageService;
    @Mock
    private ImagePort imagePort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(imageService, "uploadDir", uploadDir);
        ReflectionTestUtils.setField(imageService, "thumbnailDir", thumbnailDir);
    }

    @Test
    void testUploadImage() throws IOException {
        Path sampleImagePath = Path.of("src/test/resources/sample-image.jpg");
        Files.createDirectories(sampleImagePath.getParent());
        Files.copy(getClass().getResourceAsStream("/sample-image.jpg"), sampleImagePath, StandardCopyOption.REPLACE_EXISTING);
        MockMultipartFile file = new MockMultipartFile("file", "sample-image.jpg", "image/jpeg", Files.readAllBytes(sampleImagePath));
        doNothing().when(imagePort).create(any(ImageModel.class));

        imageService.uploadImage(file, "Test Description", List.of("PERSON"));

        verify(imagePort, times(1)).create(any(ImageModel.class));

        // 썸네일 디렉토리에서 "thumb_"로 시작하고 "sample-image.jpg"로 끝나는 파일 찾기
        Path thumbnailDir = Path.of("src/test/thumbnails");
        boolean thumbnailExists = Files.list(thumbnailDir)
                .anyMatch(path -> path.getFileName().toString().matches("thumb_.*_sample-image.jpg"));

        assertTrue(thumbnailExists, "Thumbnail should be created.");
    }

    @Test
    void testGetImages() {
        List<ImageModel> images = List.of(
                new ImageModel(1L, "test.jpg", "Test Description", List.of("PERSON"),
                        "uploads/test.jpg", "thumbnails/thumb_test.jpg")
        );

        when(imagePort.findAll()).thenReturn(images);

        List<ImageModel> result = imageService.getImages();

        assertEquals(1, result.size());
        assertEquals("test.jpg", result.get(0).getFileName());
        assertEquals("uploads/test.jpg", result.get(0).getFilePath());
        assertEquals("thumbnails/thumb_test.jpg", result.get(0).getThumbnailPath());
        verify(imagePort, times(1)).findAll();
    }

    @Test
    void testGetImageById() {
        ImageModel image = new ImageModel(1L, "test.jpg", "Test Description", List.of("PERSON"),
                "uploads/test.jpg", "thumbnails/thumb_test.jpg");

        when(imagePort.findById(1L)).thenReturn(Optional.of(image));

        ImageModel result = imageService.getImage(1L);

        assertNotNull(result);
        assertEquals("test.jpg", result.getFileName());
        assertEquals("uploads/test.jpg", result.getFilePath());
        assertEquals("thumbnails/thumb_test.jpg", result.getThumbnailPath());
        verify(imagePort, times(1)).findById(1L);
    }

    @Test
    void testDeleteImage() {
        ImageModel image = new ImageModel(1L, "test.jpg", "Test Description", List.of("PERSON"),
                "uploads/test.jpg", "thumbnails/thumb_test.jpg");

        when(imagePort.findById(1L)).thenReturn(Optional.of(image));
        doNothing().when(imagePort).deleteById(1L);

        imageService.deleteImage(1L);

        verify(imagePort, times(1)).deleteById(1L);
    }
}
