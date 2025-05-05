package com.kennel.backend.entity.abstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Getter
@Setter
@MappedSuperclass
@FilterDef(name = "softDeleteFilter")
//, parameters = @ParamDef(name = "deleted", type = Boolean.class))
public abstract class SoftDeletableEntity {
    @Column(nullable = false)
    private Boolean deleted = false;
}
