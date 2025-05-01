package com.kennel.backend.dto.vetVisit.request;

import com.kennel.backend.entity.Appointment;
import com.kennel.backend.entity.Dog;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Getter
public class VetVisitRequestDto {

    private String notes;
    private String prescription;
    private Date visitDate;
    private Long appointmentId;
}
