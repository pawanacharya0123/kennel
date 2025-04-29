package com.kennel.backend.dto.dog.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class DogRequestDTO {
    protected String name;
    protected String breed;
    protected Date dob;
    protected String description;
}
