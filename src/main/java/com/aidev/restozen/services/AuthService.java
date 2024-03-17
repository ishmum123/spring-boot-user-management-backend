package com.aidev.restozen.services;

import com.aidev.restozen.database.entities.User;
import com.aidev.restozen.database.repositories.UserRepository;
import com.aidev.restozen.helpers.components.TokenCreationComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenCreationComponent tokenCreationComponent;
    private final UserRepository userRepository;

    /** @noinspection OptionalGetWithoutIsPresent*/
    // TODO: Pass `User` from controller instead of `Authentication`
    public String generateToken(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).get();
        return tokenCreationComponent.generateToken(user);
    }
}
