package com.kennel.backend.dto.kennel.request;
import lombok.Data;

import java.util.Date;

@Data
public abstract class KennelRequestDto {
    private String name;
    private String location;
    private Date establishedAt;
}
