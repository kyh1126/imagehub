package com.example.imagehub.application.port.out;

import com.example.imagehub.domain.Image;

public interface UploadImagePort {

    void upload(Image image);

}
