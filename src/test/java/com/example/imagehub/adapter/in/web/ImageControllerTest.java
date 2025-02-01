package com.example.imagehub.adapter.in.web;

import com.example.imagehub.application.port.in.ImageUseCase;
import com.example.imagehub.domain.model.ImageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ImageControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ImageController imageController;

    @Mock
    private ImageUseCase imageUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    void testUploadImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[10]);
        doNothing().when(imageUseCase).uploadImage(any(), any(), any());

        mockMvc.perform(multipart("/images")
                        .file(file)
                        .param("description", "Test Description")
                        .param("categories", "PERSON"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image uploaded successfully"));
    }

    @Test
    void testGetImages() throws Exception {
        List<ImageModel> images = List.of(new ImageModel(
                1L, "test.jpg", "Test Description", List.of("PERSON"),
                "uploads/test.jpg", "thumbnails/thumb_test.jpg"));

        when(imageUseCase.getImages()).thenReturn(images);

        mockMvc.perform(get("/images")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fileName").value("test.jpg"))
                .andExpect(jsonPath("$[0].filePath").value("uploads/test.jpg"))
                .andExpect(jsonPath("$[0].thumbnailPath").value("thumbnails/thumb_test.jpg"));
    }

    @Test
    void testGetImageById() throws Exception {
        ImageModel image = new ImageModel(
                1L, "test.jpg", "Test Description", List.of("PERSON"),
                "uploads/test.jpg", "thumbnails/thumb_test.jpg");

        when(imageUseCase.getImage(1L)).thenReturn(image);

        mockMvc.perform(get("/images/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName").value("test.jpg"))
                .andExpect(jsonPath("$.filePath").value("uploads/test.jpg"))
                .andExpect(jsonPath("$.thumbnailPath").value("thumbnails/thumb_test.jpg"));
    }

    @Test
    void testDeleteImage() throws Exception {
        doNothing().when(imageUseCase).deleteImage(1L);

        mockMvc.perform(delete("/images/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image deleted successfully"));
    }
}
