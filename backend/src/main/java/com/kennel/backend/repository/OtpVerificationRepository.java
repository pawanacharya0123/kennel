package com.kennel.backend.repository;

import com.kennel.backend.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
}
