package com.kennel.backend.dto.clinic.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ClinicRequestDto {
    @NotNull
    private String name;
    @NotNull
    private String address;

}
