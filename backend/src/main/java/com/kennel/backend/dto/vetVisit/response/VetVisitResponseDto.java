package com.kennel.backend.dto.vetVisit.response;

import com.kennel.backend.entity.*;
import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder
public class VetVisitResponseDto {
    private Long id;
    private String notes;
    private String prescription;
    private String slug;
    private Date visitDate;
    private Dog dog;
    private Appointment appointment;
    private Clinic clinic;
    private UserEntity doctor;
    private UserEntity owner;
}
