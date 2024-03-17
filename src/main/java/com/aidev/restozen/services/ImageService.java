package com.aidev.restozen.services;

import com.aidev.restozen.helpers.dtos.IdHolderDTO;
import com.aidev.restozen.helpers.dtos.ImageDTO;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ImageService {

    private static final String IMAGE_PREFIX = "data:image/";
    private static final String DATA_SEPARATOR = ";base64,";

    @Autowired
    private ServletContext context;

    private static synchronized String createIdFromEpoch() {
        return String.valueOf(Instant.now().toEpochMilli());
    }

    public IdHolderDTO uploadImage(ImageDTO dto) {
        String imageData = dto.image();

        try {
            String fileType = imageData.split(IMAGE_PREFIX)[1].split(DATA_SEPARATOR)[0];
            byte[] bytes = Base64.getDecoder().decode(imageData.split(DATA_SEPARATOR)[1]);
            String id = createIdFromEpoch() + "." + fileType;
            String directoryPath = new FileSystemResource("tmp").getFile().getAbsolutePath();
            Path path = Paths.get(directoryPath, id);
            Files.write(path, bytes);
            return new IdHolderDTO(id);
        } catch (IOException e) {
            // TODO: Create Action Specific RuntimeException-s
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void removeStaleImages() {
        String[] files = new FileSystemResource("tmp").getFile().list();
        Arrays.stream(files)
                .filter(file -> {
                    String filenameEpoch = file.split("\\.")[0];
                    long creationEpoch = Long.parseLong(filenameEpoch);
                    Instant creationInstant = Instant.ofEpochMilli(creationEpoch);
                    return Instant.now().isAfter(creationInstant.plus(Duration.ofHours(6)));
                })
                .forEach(file -> new FileSystemResource("tmp/" + file).getFile().delete());
    }
}
