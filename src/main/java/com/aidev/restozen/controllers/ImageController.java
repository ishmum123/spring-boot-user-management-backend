package com.aidev.restozen.controllers;

import com.aidev.restozen.helpers.dtos.IdHolderDTO;
import com.aidev.restozen.helpers.dtos.ImageDTO;
import com.aidev.restozen.services.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService service;

    @PostMapping
    public IdHolderDTO uploadImage(@Valid @RequestBody ImageDTO dto) {
        return service.uploadImage(dto);
    }

}