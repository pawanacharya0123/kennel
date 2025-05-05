package com.kennel.backend.repository;

import com.kennel.backend.entity.ForgetPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword, Long> {
}
