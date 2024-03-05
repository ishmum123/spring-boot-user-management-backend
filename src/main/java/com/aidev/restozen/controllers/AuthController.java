package com.aidev.restozen.controllers;

import com.aidev.restozen.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("token")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping
    public String generateToken(Authentication authentication) {
        return service.generateToken(authentication);
    }

}