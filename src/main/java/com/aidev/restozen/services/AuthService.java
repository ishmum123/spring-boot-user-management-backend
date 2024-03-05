package com.aidev.restozen.services;

import com.aidev.restozen.helpers.components.TokenCreationComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenCreationComponent tokenCreationComponent;

    public String generateToken(Authentication authentication) {
        return tokenCreationComponent.generateToken(authentication.getAuthorities(), authentication.getName());
    }
}
