package com.aidev.restozen.services;

import com.aidev.restozen.database.entities.User;
import com.aidev.restozen.database.repositories.UserRepository;
import com.aidev.restozen.helpers.components.ConverterComponent;
import com.aidev.restozen.helpers.components.TokenCreationComponent;
import com.aidev.restozen.helpers.components.UserCreationComponent;
import com.aidev.restozen.helpers.dtos.CustomerCreationDTO;
import com.aidev.restozen.helpers.dtos.EmployeeCreationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserCreationComponent userCreationComponent;
    private final TokenCreationComponent tokenCreationComponent;
    private final UserRepository userRepository;

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public User addEmployee(EmployeeCreationDTO dto) {
        String imageId = dto.profileImageId();
        String location = moveFileToPublicDirectory(imageId);
        return userCreationComponent.createEmployee(dto, location);
    }

    public String registerCustomer(CustomerCreationDTO dto) {
        String imageId = dto.profileImageId();
        String location = moveFileToPublicDirectory(imageId);
        User user = userCreationComponent.createCustomer(dto, location);
        return tokenCreationComponent.generateToken(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .map(ConverterComponent::convert)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    // TODO: Move all file related operations in a component
    private static String moveFileToPublicDirectory(String fileId) {
        if (fileId == null) {
            return null;
        }
        File tmpFile = new FileSystemResource("tmp/" + fileId).getFile();
        if (!tmpFile.exists()) {
            throw new RuntimeException("Invalid File Name Provided");
        }

        String extension = fileId.split("\\.")[1];
        String permanentId = UUID.randomUUID() + "." + extension;

        String publicDirectoryPath = new FileSystemResource("public").getFile().getAbsolutePath();
        Path publicFilePath = Paths.get(publicDirectoryPath, permanentId);
        try {
            Files.write(publicFilePath, Files.readAllBytes(tmpFile.toPath()));
        } catch (IOException e) {
            throw new RuntimeException("Could not copy tmp file");
        }

        return "resources/" + permanentId;
    }
}
