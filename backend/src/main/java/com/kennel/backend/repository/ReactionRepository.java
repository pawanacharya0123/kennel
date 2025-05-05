package com.kennel.backend.repository;

import com.kennel.backend.entity.Reaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Page<Reaction> findByPostSlug(String postSlug, Pageable pageable);

    Page<Reaction> findByCommentSlug(String commentSlug, Pageable pageable);

    Optional<Reaction> findBySlug(String slug);
}
