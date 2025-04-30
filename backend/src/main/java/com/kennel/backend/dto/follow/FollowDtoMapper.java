package com.kennel.backend.dto.follow;

import com.kennel.backend.dto.follow.request.FollowRequestDto;
import com.kennel.backend.dto.follow.response.FollowResponseDto;
import com.kennel.backend.entity.Follower;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowDtoMapper {
    private final UserEntityRepository userEntityRepository;
    public Follower toEntity(FollowRequestDto followRequestDto){
        UserEntity userToFollow= userEntityRepository.findById(followRequestDto.getFollowing())
                .orElseThrow(()-> new EntityNotFoundException(UserEntity.class, "id", followRequestDto.getFollowing()));

        String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentAuthUser= userEntityRepository.findByEmail(authUserEmail)
                .orElseThrow(()-> new EntityNotFoundException(UserEntity.class, "email", authUserEmail));

        return Follower.builder()
                .follower(currentAuthUser)
                .following(userToFollow)
                .build();
    }

    public FollowResponseDto toDto(Follower follower){
        return FollowResponseDto.builder()
                .following(follower.getFollowing())
                .follower(follower.getFollower())
                .build();
    }

    public List<FollowResponseDto> toDto(List<Follower> followers){
        return followers.stream()
                .map(this::toDto)
                .toList();
    }

}
