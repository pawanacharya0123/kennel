package com.kennel.backend.repository;

import com.kennel.backend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostId(Long postId, Pageable pageable);

    Page<Comment> findByPostSlug(String postSlug, Pageable pageable);

    Optional<Comment> findBySlug(String slug);

    Page<Comment> findByPostSlugAndDeletedFalse(String postSlug, Pageable pageable);

    Optional<Comment> findBySlugAndDeletedFalse(String slug);

    boolean existsBySlug(String slug);
}
