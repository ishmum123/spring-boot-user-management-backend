package com.aidev.restozen.services;

import com.aidev.restozen.helpers.dtos.IdHolderDTO;
import com.aidev.restozen.helpers.dtos.ImageDTO;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private static final String IMAGE_PREFIX = "data:image/";
    private static final String DATA_SEPARATOR = ";base64,";

    @Autowired
    private ServletContext context;

    public IdHolderDTO uploadImage(ImageDTO dto) {
        UUID id = UUID.randomUUID();
        String imageData = dto.image();

        try {
            String fileType = imageData.split(IMAGE_PREFIX)[1].split(DATA_SEPARATOR)[0];
            byte[] bytes = Base64.getDecoder().decode(imageData.split(DATA_SEPARATOR)[1]);
            String directoryPath = new FileSystemResource("tmp").getFile().getAbsolutePath();
            Path path = Paths.get(directoryPath , id + "." + fileType);
            Files.write(path, bytes);
            return new IdHolderDTO(id.toString());
        } catch (IOException e) {
            // TODO: Create Action Specific RuntimeException-s
            throw new RuntimeException(e);
        }
    }
}
