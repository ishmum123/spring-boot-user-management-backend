package com.aidev.restozen.services;

import com.aidev.restozen.database.entities.Credential;
import com.aidev.restozen.database.repositories.CredentialRepository;
import com.aidev.restozen.helpers.components.ConverterComponent;
import com.aidev.restozen.helpers.components.TokenCreationComponent;
import com.aidev.restozen.helpers.components.UserCreationComponent;
import com.aidev.restozen.helpers.dtos.CredentialCreationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aidev.restozen.helpers.components.ConverterComponent.convert;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserCreationComponent userCreationComponent;
    private final TokenCreationComponent tokenCreationComponent;
    private final CredentialRepository credentialRepository;

    public List<Credential> listUsers() {
        return credentialRepository.findAll();
    }

    public Credential createEmployee(CredentialCreationDTO dto) {
        return userCreationComponent.createUser(dto, "EMPLOYEE");
    }

    public String registerCustomer(CredentialCreationDTO dto) {
        Credential credential = userCreationComponent.createUser(dto, "CUSTOMER");
        UserDetails userDetails = convert(credential);
        return tokenCreationComponent.generateToken(userDetails.getAuthorities(), userDetails.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return credentialRepository
                .findByUsername(username)
                .map(ConverterComponent::convert)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
