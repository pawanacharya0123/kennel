package com.kennel.backend.security.dtos;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationRequest {
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    private Long otp;
}
