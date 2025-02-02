package com.example.imagehub.application.service;

import com.example.imagehub.application.port.in.UploadImageCommand;
import com.example.imagehub.application.port.out.LoadImagePort;
import com.example.imagehub.application.port.out.UpdateImagePort;
import com.example.imagehub.application.port.out.UploadImagePort;
import com.example.imagehub.domain.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    private final String uploadDir = "src/test/uploads";
    private final String thumbnailDir = "src/test/thumbnails";

    @Spy
    @InjectMocks
    private ImageService imageService;
    @Mock
    private UploadImagePort uploadImagePort;
    @Mock
    private LoadImagePort loadImagePort;
    @Mock
    private UpdateImagePort updateImagePort;
    @Mock
    private Pageable pageRequest;

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
        doNothing().when(uploadImagePort).upload(any(Image.class));

        UploadImageCommand uploadImageCommand = new UploadImageCommand(file, "Test Description", List.of("PERSON"));
        imageService.uploadImage(uploadImageCommand);

        verify(uploadImagePort, times(1)).upload(any(Image.class));

        // 썸네일 디렉토리에서 "thumb_"로 시작하고 "sample-image.jpg"로 끝나는 파일 찾기
        Path thumbnailDir = Path.of("src/test/thumbnails");
        boolean thumbnailExists = Files.list(thumbnailDir)
                .anyMatch(path -> path.getFileName().toString().matches("thumb_.*_sample-image.jpg"));

        assertTrue(thumbnailExists, "Thumbnail should be created.");
    }

    @Test
    void testGetImages() {
        // 빈 바이트 배열 (10바이트)
        var file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[10]);
        UploadImageCommand uploadImageCommand = new UploadImageCommand(file, "Test Description", List.of("PERSON"));
        Image image = Image.of(uploadImageCommand, "test.jpg", uploadDir, thumbnailDir + "/thumb_test.jpg");
        List<Image> images = List.of(image);

        when(loadImagePort.getImages(any(Pageable.class))).thenReturn(images);

        var result = imageService.getImages(PageRequest.ofSize(10));

        assertEquals(1, result.size());

        var firstImage = result.getFirst();
        assertEquals("test.jpg", firstImage.getFileName());
        assertEquals(uploadDir + "/test.jpg", firstImage.getFilePath());
        assertEquals(thumbnailDir + "/thumb_test.jpg", firstImage.getThumbnailPath());
        verify(loadImagePort, times(1)).getImages(any(Pageable.class));
    }

    @Test
    void testGetImageById() {
        // 빈 바이트 배열 (10바이트)
        var file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[10]);
        UploadImageCommand uploadImageCommand = new UploadImageCommand(file, "Test Description", List.of("PERSON"));
        Image image = Image.of(uploadImageCommand, "test.jpg", uploadDir, thumbnailDir + "/thumb_test.jpg");

        when(loadImagePort.getImage(1L)).thenReturn(image);

        var result = imageService.getImage(1L);

        assertNotNull(result);
        assertEquals("test.jpg", result.getFileName());
        assertEquals(uploadDir + "/test.jpg", result.getFilePath());
        assertEquals(thumbnailDir + "/thumb_test.jpg", result.getThumbnailPath());
        verify(loadImagePort, times(1)).getImage(1L);
    }

    @Test
    void testDeleteImage() {
        // 빈 바이트 배열 (10바이트)
        var file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[10]);
        UploadImageCommand uploadImageCommand = new UploadImageCommand(file, "Test Description", List.of("PERSON"));
        Image image = Image.of(uploadImageCommand, "test.jpg", uploadDir, thumbnailDir + "/thumb_test.jpg");

        when(loadImagePort.getImage(1L)).thenReturn(image);
        doNothing().when(updateImagePort).deleteById(1L);

        imageService.deleteImage(1L);

        verify(updateImagePort, times(1)).deleteById(1L);
    }
}
