package com.kennel.backend.repository;

import com.kennel.backend.entity.Friend;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.FriendStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    boolean existsBySenderAndReceiverAndStatus(UserEntity user, UserEntity receiver, FriendStatus friendStatus);
    Optional<Friend> findBySenderAndReceiverAndStatus(UserEntity user, UserEntity receiver, FriendStatus friendStatus);
    Page<Friend> findBySenderOrReceiverAndStatus(UserEntity currentAuthUser, UserEntity currentAuthUser1, FriendStatus friendStatus, Pageable pageable);
    Page<Friend> findBySenderAndStatus(UserEntity currentAuthUser, FriendStatus friendStatus, Pageable pageable);
    Page<Friend> findByReceiverAndStatus(UserEntity currentAuthUser, FriendStatus friendStatus, Pageable pageable);
}
