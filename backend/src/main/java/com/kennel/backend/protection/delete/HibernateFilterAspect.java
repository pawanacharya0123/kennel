package com.kennel.backend.protection.delete;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


import com.kennel.backend.protection.customAnnotation.EnableSoftDeleteFilter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Aspect
@Component
@RequiredArgsConstructor
public class HibernateFilterAspect {
    private final EntityManager entityManager;

    @Around("@annotation(com.kennel.backend.protection.customAnnotation.EnableSoftDeleteFilter)")
    @Transactional
    public Object enableSoftDeleteFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("softDeleteFilter");

        try {
            return joinPoint.proceed(); // Execute the target method
        } finally {
            session.disableFilter("softDeleteFilter"); // Cleanup to avoid leaking into other methods
        }
    }
}
