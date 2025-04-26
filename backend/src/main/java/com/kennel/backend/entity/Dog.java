package com.kennel.backend.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String breed;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob;

    private String description;
    private Boolean isForSale= false;
    private float price;

    @ManyToOne
    @JoinColumn(name = "dog_owner_id", nullable = false)
    private UserEntity owner;

    @OneToMany(mappedBy = "dog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "dog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VetVisit> vetVisits;

    @ManyToOne
    @JoinColumn(name = "kennel_id", nullable = true)
    private Kennel kennel;

    @OneToMany(mappedBy = "dog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vaccine> vaccines;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
