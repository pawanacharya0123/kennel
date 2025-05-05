package com.kennel.backend.repository;

import com.kennel.backend.entity.Follower;
import com.kennel.backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
//    List<Follower>  findByFollowerId(Long id);
    Page<Follower> findByFollower(UserEntity follower, Pageable pageable);
    boolean existsByFollowerAndFollowing(UserEntity follower, UserEntity following);
    Optional<Follower> findByFollowerAndFollowing(UserEntity follower, UserEntity following);
    Page<Follower> findByFollowing(UserEntity following, Pageable pageable);
}
