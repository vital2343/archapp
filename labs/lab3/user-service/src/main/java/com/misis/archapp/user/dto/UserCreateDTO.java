package com.misis.archapp.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserCreateDTO(
    @NotBlank String name,
    @NotBlank String email
) {
}
