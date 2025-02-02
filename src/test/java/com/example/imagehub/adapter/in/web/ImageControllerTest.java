package com.example.imagehub.adapter.in.web;

import com.example.imagehub.adapter.in.ImageController;
import com.example.imagehub.application.port.in.ImageUseCase;
import com.example.imagehub.application.port.in.UploadImageCommand;
import com.example.imagehub.application.port.out.ImageResponse;
import com.example.imagehub.domain.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
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

    private final PageRequest pageRequest = PageRequest.ofSize(10);
    private MockMvc mockMvc;
    @InjectMocks
    private ImageController imageController;
    @Mock
    private ImageUseCase imageUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
    }

    @Test
    void testUploadImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[10]);
        String jsonRequest = "{\"description\":\"Test Description\",\"categories\":[\"PERSON\"]}";
        MockMultipartFile requestPart = new MockMultipartFile(
                "request", "test.jpg", "application/json", jsonRequest.getBytes()
        );

        doNothing().when(imageUseCase).uploadImage(any());

        mockMvc.perform(multipart("/images")
                        .file(file)
                        .file(requestPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image uploaded successfully"));
    }

    @Test
    void testGetImages() throws Exception {
        // 빈 바이트 배열 (10바이트)
        var file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[10]);
        UploadImageCommand uploadImageCommand = new UploadImageCommand(file, "Test Description", List.of("PERSON"));
        Image image = Image.of(uploadImageCommand, "test.jpg", "uploads", "thumbnails/thumb_test.jpg");
        var images = List.of(ImageResponse.from(image));

        when(imageUseCase.getImages(pageRequest)).thenReturn(images);

        mockMvc.perform(get("/images")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fileName").value("test.jpg"))
                .andExpect(jsonPath("$[0].filePath").value("uploads/test.jpg"))
                .andExpect(jsonPath("$[0].thumbnailPath").value("thumbnails/thumb_test.jpg"));
    }

    @Test
    void testGetImageById() throws Exception {
        // 빈 바이트 배열 (10바이트)
        var file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[10]);
        UploadImageCommand uploadImageCommand = new UploadImageCommand(file, "Test Description", List.of("PERSON"));
        Image image = Image.of(uploadImageCommand, "test.jpg", "uploads", "thumbnails/thumb_test.jpg");

        when(imageUseCase.getImage(1L)).thenReturn(ImageResponse.from(image));

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
