package com.aidev.restozen.helpers.components;

import com.aidev.restozen.database.entities.Credential;
import com.aidev.restozen.database.repositories.CredentialRepository;
import com.aidev.restozen.database.repositories.UserTypeRepository;
import com.aidev.restozen.helpers.dtos.CredentialCreationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreationComponent {

    private final CredentialRepository credentialRepository;
    private final UserTypeRepository userTypeRepository;

    private final PasswordEncoder encoder;

    public Credential createUser(CredentialCreationDTO dto, String userType) {
        return userTypeRepository
                .findByName(userType)
                .map(type -> Credential
                        .builder()
                        .username(dto.username())
                        .password(encoder.encode(dto.password()))
                        .type(type)
                        .build())
                .map(credentialRepository::saveAndFlush)
                .orElseThrow(() -> new IllegalArgumentException("User Type '" + userType + "' does not exist"));
    }
}
