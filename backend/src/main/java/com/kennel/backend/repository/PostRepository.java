package com.kennel.backend.repository;

import com.kennel.backend.entity.Post;
import com.kennel.backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
    void deleteBySlug(String slug);
    List<Post> findByCreatedById(Long userId);
    Boolean existsBySlug(String slug);
    Page<Post> findByCreatedBy(UserEntity currentAuthUser, Pageable pageable);
    List<Post> findByCreatedBy(UserEntity currentAuthUser);
    Optional<Post> findByIdAndDeletedFalse(Long id);
    Page<Post> findByCreatedByAndDeletedFalse(UserEntity currentAuthUser, Pageable pageable);
    Optional<Post> findBySlugAndDeletedFalse(String slug);
    boolean existsBySlugAndDeletedFalse(String slug);
}
