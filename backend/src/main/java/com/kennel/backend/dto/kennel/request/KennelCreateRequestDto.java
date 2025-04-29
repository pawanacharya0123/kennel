package com.kennel.backend.dto.kennel.request;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class KennelCreateRequestDto extends KennelRequestDto{
    @Override
    @NotNull
    public String getName() {
        return super.getName();
    }

    @Override
    @NotNull
    public String getLocation() {
        return super.getLocation();
    }

    @Override
    @NotNull
    public Date getEstablishedAt() {
        return super.getEstablishedAt();
    }
}
