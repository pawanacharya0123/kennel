package com.kennel.backend.dto.dog.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class DogCreateRequestDTO extends DogRequestDTO {
    @Override
    @NotBlank
    public String getName(){
        return super.getName();
    };

    @Override
    @NotBlank
    public String getBreed(){
        return super.getBreed();
    };

    @Override
    @NotNull
    public Date getDob(){
        return super.getDob();
    };

}
