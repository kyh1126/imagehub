package com.example.imagehub.application.service;

import com.example.imagehub.application.port.out.ImageRepositoryPort;
import com.example.imagehub.domain.model.ImageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ImageServiceTest {
    @Spy
    @InjectMocks
    private ImageService imageService;

    @Mock
    private ImageRepositoryPort imageRepository;

    //    @Value("${app.upload.dir}")
    private String uploadDir = "src/test/uploads";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(imageService, "uploadDir", uploadDir);
    }

    @Test
    void testUploadImage() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[10]);
        doNothing().when(imageRepository).save(any(ImageModel.class));

        imageService.uploadImage(file, "Test Description", List.of("PERSON"));

        verify(imageRepository, times(1)).save(any(ImageModel.class));
    }

    @Test
    void testGetImages() {
        List<ImageModel> images = List.of(new ImageModel(1L, "test.jpg", "Test Description", List.of("PERSON")));
        when(imageRepository.findAll()).thenReturn(images);

        List<ImageModel> result = imageService.getImages();

        assertEquals(1, result.size());
        assertEquals("test.jpg", result.get(0).getFileName());
        verify(imageRepository, times(1)).findAll();
    }

    @Test
    void testGetImageById() {
        ImageModel image = new ImageModel(1L, "test.jpg", "Test Description", List.of("PERSON"));
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        ImageModel result = imageService.getImage(1L);

        assertNotNull(result);
        assertEquals("test.jpg", result.getFileName());
        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteImage() {
        ImageModel image = new ImageModel(1L, "test.jpg", "Test Description", List.of("PERSON"));
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
        doNothing().when(imageRepository).deleteById(1L);

        imageService.deleteImage(1L);

        verify(imageRepository, times(1)).deleteById(1L);
    }
}
