package com.kennel.backend.entity;

import com.kennel.backend.entity.abstractEntity.SoftDeletableEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@Table(
    name = "dogs",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"name", "dog_owner_id"})
    }
)
@Filter(name = "softDeleteFilter", condition = "deleted = false")
public class Dog extends SoftDeletableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String breed;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date dob;

    @Nullable
    private String description;

    @NotNull
    private Boolean isForSale= false;
    private Float price;

    @Column(unique = true)
    private String slug;

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
    @Filter(name = "softDeleteFilter", condition = "deleted = false")
    private List<VaccineRecord> vaccines;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    @PreUpdate
    private void validateForSaleAndPrice() {
        if (Boolean.TRUE.equals(isForSale) && (price == null || price < 0)) {
            throw new IllegalStateException("Price must be set if dog is for sale.");
        }
    }
}
