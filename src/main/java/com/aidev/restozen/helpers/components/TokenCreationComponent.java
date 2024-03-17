package com.aidev.restozen.helpers.components;

import com.aidev.restozen.database.entities.User;
import com.aidev.restozen.database.entities.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenCreationComponent {

    private final JwtEncoder encoder;

    public String generateToken(User user) {
        Instant now = Instant.now();
        String roles = user
                .getTypes()
                .stream()
                .map(UserType::getName)
                .collect(Collectors.joining(","));
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(60 * 60))
                .subject(user.getUsername())
                .claim("roles", roles)
                .claim("name", user.getName());

        if (user.getImageLocation() != null) {
            builder.claim("imageLocation", user.getImageLocation());
        }

        JwtClaimsSet claims = builder.build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
