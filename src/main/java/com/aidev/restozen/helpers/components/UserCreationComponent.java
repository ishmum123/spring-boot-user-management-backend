package com.aidev.restozen.helpers.components;

import com.aidev.restozen.database.entities.User;
import com.aidev.restozen.database.entities.UserType;
import com.aidev.restozen.database.repositories.UserRepository;
import com.aidev.restozen.database.repositories.UserTypeRepository;
import com.aidev.restozen.helpers.dtos.CustomerCreationDTO;
import com.aidev.restozen.helpers.dtos.EmployeeCreationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserCreationComponent {

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;

    private final PasswordEncoder encoder;

    public User createEmployee(EmployeeCreationDTO dto, String imageLocation) {
        Set<String> roles = dto.roles();

        Set<UserType> userTypes = userTypeRepository.findByNameIn(roles);

        if (userTypes.size() < roles.size())
            throw new IllegalArgumentException("One or more UserType-s do not exist");

        User user = User.builder()
                .name(dto.name())
                .username(dto.username())
                .password(encoder.encode(dto.password()))
                .imageLocation(imageLocation)
                .types(userTypes)
                .build();

        return userRepository.saveAndFlush(user);
    }

    public User createCustomer(CustomerCreationDTO dto, String imageLocation) {
        //noinspection OptionalGetWithoutIsPresent
        return userTypeRepository
                .findByName("CUSTOMER")
                .map(type -> User
                        .builder()
                        .name(dto.name())
                        .username(dto.username())
                        .password(encoder.encode(dto.password()))
                        .imageLocation(imageLocation)
                        .types(Set.of(type))
                        .build())
                .map(userRepository::saveAndFlush)
                .get();
    }
}
