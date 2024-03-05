package com.aidev.restozen.helpers.dtos;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record CredentialCreationDTO(@NotEmpty @Length(min = 3, max = 20) String username, @NotEmpty @Length(min = 6, max = 50) String password) {
}
