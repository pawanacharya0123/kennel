package com.kennel.backend.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String description;

    @Column(unique = true)
    private String slug;

    @ManyToOne
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;

    @ManyToOne
    @JoinColumn(name = "vet_visit_id", nullable = false)
    private VetVisit vetVisit;

    @ManyToOne
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private UserEntity doctor;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date vaccinationDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
