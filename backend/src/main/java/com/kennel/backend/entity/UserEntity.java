package com.kennel.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotNull
    @Size(max = 255)
    private String email;

    @JsonIgnore
    @NotNull
    @Size(min = 8)
    private String password;

//    public UserEntity(String email, String password){
//        this.email=email;
//        this.password= password;
//    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_users",
            joinColumns = @JoinColumn(name= "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Nullable
    private String extraInfo;

    @OneToMany(mappedBy = "owner")
    @Filter(name = "softDeleteFilter", condition = "deleted = false")
    private List<Dog> dogs;

    @OneToOne(mappedBy = "manager", fetch = FetchType.LAZY)
    private Clinic clinic;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @Filter(name = "softDeleteFilter", condition = "deleted = false")
    private List<Post> posts;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @Filter(name = "softDeleteFilter", condition = "deleted = false")
    private List<Comment> comments;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions;

    @OneToMany(mappedBy = "owner")
    private List<Appointment> appointmentsAsOwner;

    @OneToMany(mappedBy = "owner")
    private List<VetVisit> vetVisitsAsDoctor;

    @OneToMany(mappedBy = "doctor")
    private List<VetVisit> vetVisitsAsOwner;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointmentsAsDoctor;

    @OneToMany(mappedBy = "doctor")
    private List<VaccineRecord> vaccinatedAsDoctor;

    @OneToMany(mappedBy = "vaccineCreator")
    private List<Vaccine> vaccines;

    @ManyToMany(mappedBy = "doctors")
    private Set<Clinic> clinics= new HashSet<>();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "otp_verification_id", nullable = true)
    private OtpVerification otpVerification;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "forget_password_id", nullable = true)
    private ForgetPassword forgetPassword;

    private boolean isVerified= false;
}
