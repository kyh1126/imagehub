package com.example.imagehub.application.service;

import com.example.imagehub.application.port.in.ImageUseCase;
import com.example.imagehub.application.port.out.ImageRepositoryPort;
import com.example.imagehub.domain.model.ImageModel;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImageService implements ImageUseCase {

    private final ImageRepositoryPort imageRepository;
    private final Set<String> validCategories = Set.of("PERSON", "LANDSCAPE", "ANIMAL", "FOOD", "OTHERS");

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.thumbnail.dir}")
    private String thumbnailDir;

    @Override
    public void uploadImage(MultipartFile file, String description, List<String> categories) {
        validateCategories(categories);
        String fileName = saveFile(file);
        String thumbnailPath = generateThumbnail(fileName);

        ImageModel image = new ImageModel(
                null,
                fileName,
                description,
                categories,
                Path.of(uploadDir, fileName).toString(),  // 원본 이미지 경로
                thumbnailPath  // 썸네일 경로
        );
        imageRepository.save(image);
    }

    @Override
    public List<ImageModel> getImages() {
        return imageRepository.findAll();
    }

    @Override
    public ImageModel getImage(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    @Override
    public List<String> getImageCategories(Long id) {
        return imageRepository.findCategoriesById(id);
    }

    @Override
    public void deleteImage(Long id) {
        ImageModel image = getImage(id);
        deleteFile(image.getFilePath());  // 원본 이미지 삭제
        deleteFile(image.getThumbnailPath()); // 썸네일 삭제
        imageRepository.deleteById(id);
    }

    @Override
    public void addCategoriesToImage(Long id, List<String> categories) {
        validateCategories(categories);
        ImageModel image = getImage(id);
        image.getCategories().addAll(categories);
        imageRepository.save(image);
    }

    @Override
    public void removeCategoriesFromImage(Long id, List<String> categories) {
        validateCategories(categories);
        ImageModel image = getImage(id);
        image.getCategories().removeAll(categories);
        imageRepository.save(image);
    }

    private void validateCategories(List<String> categories) {
        for (String category : categories) {
            if (!validCategories.contains(category)) {
                throw new IllegalArgumentException("Invalid category: " + category);
            }
        }
    }

    String saveFile(MultipartFile file) {
        try {
            String formatName = getImageFormat(Objects.requireNonNull(file.getOriginalFilename()).toLowerCase());

            // 지원하는 이미지 형식인지 확인
            if (formatName == null) {
                throw new RuntimeException("Unsupported image format: " + formatName);
            }

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

//            // InputStream이 여러 번 사용될 수 있도록 새로 열기
//            try (InputStream inputStream = Files.newInputStream(inputPath)) {
//                Thumbnails.of(inputStream)
//                        .size(100, 100)
//                        .toFile(outputPath.toFile());
//            }
            BufferedImage img = ImageIO.read(inputPath.toFile());
            Thumbnails.of(img)
                    .size(100, 100)
                    .toFile(outputPath.toFile());

            return thumbnailName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate thumbnail", e);
        }
    }

    private String getImageFormat(String fileName) {
        try {
            String[] formatNames = ImageIO.getReaderFormatNames();

            return Arrays.stream(formatNames)
                    .filter(format -> fileName.endsWith("." + format.toLowerCase()))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
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
