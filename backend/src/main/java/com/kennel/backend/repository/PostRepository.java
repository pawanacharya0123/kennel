package com.kennel.backend.repository;

import com.kennel.backend.entity.Post;
import com.kennel.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
    void deleteBySlug(String slug);
    List<Post> findByCreatedById(Long userId);
    Boolean existsBySlug(String slug);
    List<Post> findByCreatedBy(UserEntity currentAuthUser);
}
