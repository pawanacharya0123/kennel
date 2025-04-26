package com.kennel.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kennel.backend.entity.enums.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @JsonIgnore
    private String password;
    private String role;
//    private List<String> role;
//    private Role role
    @Nullable
    private String extraInfo;

    @OneToMany(mappedBy = "owner")
    private List<Dog> dogs;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = true)
    private Clinic clinic;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions;

    @OneToMany(mappedBy = "owner")
    private List<Appointment> appointmentsAsDogParent;

    @OneToMany(mappedBy = "doctor")
    private List<VetVisit> vetVisitsAsDoctor;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointmentsAsDoctor;

    @OneToMany(mappedBy = "doctor")
    private List<Vaccine> vaccinatedAsDoctor;
}
