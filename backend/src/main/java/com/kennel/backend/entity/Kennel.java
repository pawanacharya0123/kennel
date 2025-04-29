package com.kennel.backend.entity;

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
@Data
@Builder(toBuilder = true)
@Table(
    name = "kennels",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"name", "location"})
    }
)
public class Kennel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    @NotNull
    private String location;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date establishedAt;

    @OneToMany(mappedBy = "kennel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dog> dogs;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(unique = true)
    private String slug;
}
