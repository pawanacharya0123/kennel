package com.kennel.backend.entity;

import com.kennel.backend.entity.abstractEntity.SoftDeletableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(
        name = "clinics",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "address"})
        }
)
@Filter(name = "softDeleteFilter", condition = "deleted = false")
public class Clinic extends SoftDeletableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String address;

    @OneToMany(mappedBy = "clinic")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "clinic")
    private List<VetVisit> vetVisits;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private UserEntity manager;

    @OneToMany(mappedBy = "clinic")
    @Filter(name = "softDeleteFilter", condition = "deleted = false")
    private List<VaccineRecord> vaccines;

    @ManyToMany
    @JoinTable(
            name= "clinic_doctors",
            joinColumns= @JoinColumn(name = "clinic_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    private Set<UserEntity> doctors= new HashSet<>();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(unique = true)
    private String slug;

//    @PrePersist
//    @PreUpdate
//    private void validateForSaleAndPrice() {
//        if (Boolean.TRUE.equals(isForSale) && (price == null || price < 0)) {
//            throw new IllegalStateException("Price must be set if dog is for sale.");
//        }
//    }
}
