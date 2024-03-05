package com.aidev.restozen.helpers.components;


import com.aidev.restozen.database.entities.Credential;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static java.util.Collections.singletonList;

@Component
public class ConverterComponent {
    public static UserDetails convert(Credential credential) {
        return new UserDetails() {

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return singletonList(() -> credential.getType().getName());
            }

            @Override
            public String getPassword() {
                return credential.getPassword();
            }

            @Override
            public String getUsername() {
                return credential.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}
