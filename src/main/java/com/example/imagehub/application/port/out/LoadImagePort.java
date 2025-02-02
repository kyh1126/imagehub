package com.example.imagehub.application.port.out;

import com.example.imagehub.domain.Image;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoadImagePort {

    List<Image> getImages(Pageable pageable);

    Image getImage(Long id);

    List<String> findCategoriesById(Long id);

}
