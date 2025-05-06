package com.kennel.backend.dto.kennel.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class KennelCreateRequestDto extends KennelRequestDto{
    @Override
    @NotBlank
    public String getName() {
        return super.getName();
    }

    @Override
    @NotBlank
    public String getLocation() {
        return super.getLocation();
    }

    @Override
    @NotNull
    public Date getEstablishedAt() {
        return super.getEstablishedAt();
    }
}
