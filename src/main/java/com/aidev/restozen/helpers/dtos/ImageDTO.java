package com.aidev.restozen.helpers.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record ImageDTO(@NotEmpty @Pattern(regexp = "^data:image/[a-z]+;base64,(.*)$") String image) {
}
