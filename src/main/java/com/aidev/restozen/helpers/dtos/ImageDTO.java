package com.aidev.restozen.helpers.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record ImageDTO(
        @NotEmpty
        // base64 increases the bytes count by ~1.37.
        // We are multiplying 10 mb by 1.4 (~1.37) here.
        // ref: https://en.wikipedia.org/wiki/Base64
        @Length(max = 14 * 1024 * 1024, message = "must be smaller than 10 mb")
        @Pattern(regexp = "^data:image/[a-z]+;base64,(.*)$", message = "must contain an encoded image")
        String image) {
}
