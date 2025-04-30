package com.kennel.backend.repository;

import com.kennel.backend.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByPostSlug(String postSlug);

    List<Reaction> findByCommentSlug(String commentSlug);

    Optional<Reaction> findBySlug(String slug);
}
