package com.aidev.restozen.helpers.components;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenCreationComponent {

    private final JwtEncoder encoder;

    public String generateToken(Collection<? extends GrantedAuthority> authorities, String name) {
        Instant now = Instant.now();
        long expiry = 3600L;
        // @formatter:off
        String scope = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(name)
                .claim("roles", scope)
                .build();
        // @formatter:on
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
