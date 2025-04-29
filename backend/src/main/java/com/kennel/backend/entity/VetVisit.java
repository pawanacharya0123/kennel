package com.kennel.backend.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VetVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String notes;

    @Nullable
    private String prescription;

    @Column(unique = true)
    private String slug;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date visitDate;

    @ManyToOne
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private UserEntity doctor;

    @OneToMany(mappedBy = "vetVisit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Vaccine> vaccines;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
