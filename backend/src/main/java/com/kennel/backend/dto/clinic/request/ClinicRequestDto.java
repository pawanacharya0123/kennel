package com.kennel.backend.dto.clinic.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ClinicRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String address;

}
