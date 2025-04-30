package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.follow.FollowDtoMapper;
import com.kennel.backend.dto.follow.request.FollowRequestDto;
import com.kennel.backend.dto.follow.response.FollowResponseDto;
import com.kennel.backend.entity.Follower;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.repository.FollowerRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.FollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowerServiceImpl implements FollowerService {
    private final UserEntityRepository userEntityRepository;
    private final FollowerRepository followerRepository;
    private final FollowDtoMapper followDtoMapper;
    private final AuthUtility authUtility;

    @Override
    public FollowResponseDto follow(FollowRequestDto followRequestDto) {

        Follower follow = followDtoMapper.toEntity(followRequestDto);

        if(followerRepository.existsByFollowerAndFollowing(follow.getFollower(), follow.getFollowing())){
            return followDtoMapper.toDto(
                    followerRepository.findByFollowerAndFollowing(follow.getFollower(), follow.getFollowing())
                            .orElseThrow(()-> new EntityNotFoundException(Follower.class, "follower", follow.getFollower().getId()))
            );
        }

        return followDtoMapper.toDto(followerRepository.save(follow));
    }

    @Override
    public void unfollow(Long id) {
        Follower follow = followerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Follower.class, "id", id));

        UserEntity currentAuthUser= authUtility.getCurrentUser();

        if(!follow.getFollower().getId().equals(currentAuthUser.getId())){
            throw new IllegalStateException("You are not following this user");
        }

        followerRepository.delete(follow);
    }

    @Override
    public void unfollowByUser(Long userId) {
        UserEntity currentAuthUser= authUtility.getCurrentUser();

        UserEntity followed = userEntityRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, "id", userId));

        Optional<Follower> follow= followerRepository.findByFollowerAndFollowing(currentAuthUser,followed);

        if(follow.isEmpty()){
            throw new EntityNotFoundException(Follower.class, "following", userId);
        }
        followerRepository.delete(follow.get());
    }

    @Override
    public List<FollowResponseDto> getAllFollowing() {
        UserEntity currentAuthUser=authUtility.getCurrentUser();

        return followDtoMapper.toDto(followerRepository.findByFollower(currentAuthUser));
    }

    @Override
    public List<FollowResponseDto> getAllFollowers() {
        UserEntity currentAuthUser= authUtility.getCurrentUser();

        return followDtoMapper.toDto(followerRepository.findByFollowing(currentAuthUser));
    }
}
