package com.example.imagehub.application.port.out;

import com.example.imagehub.domain.Image;

public interface UpdateImagePort {

    void update(Image image);

    void deleteById(Long id);

}
