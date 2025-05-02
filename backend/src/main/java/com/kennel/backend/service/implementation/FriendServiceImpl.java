package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.friend.FriendToDtoMapper;
import com.kennel.backend.dto.friend.request.FriendRequestDto;
import com.kennel.backend.dto.friend.response.FriendResponseDto;
import com.kennel.backend.entity.Friend;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.FriendStatus;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.exception.ForbiddenActionException;
import com.kennel.backend.repository.FriendRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final UserEntityRepository userEntityRepository;
    private final FriendToDtoMapper friendToDtoMapper;
    private final AuthUtility authUtility;

    @Override
    public FriendResponseDto sendFriendRequest(FriendRequestDto friendRequestDto) {
        UserEntity currentAuthUser= authUtility.getCurrentUser();

        Friend friendship = friendToDtoMapper.toEntity(friendRequestDto);

        boolean alreadyFriends =friendRepository
                .existsBySenderAndReceiverAndStatus(currentAuthUser, friendship.getReceiver(), FriendStatus.ACCEPTED)
                ||
                friendRepository
                        .existsBySenderAndReceiverAndStatus(friendship.getReceiver(), currentAuthUser, FriendStatus.ACCEPTED);
        if(alreadyFriends){
            throw new IllegalStateException("You are already friends with this user");
        }

        boolean pendingRequestExists= friendRepository
                .existsBySenderAndReceiverAndStatus(currentAuthUser, friendship.getReceiver(), FriendStatus.REQUESTED);
        if(pendingRequestExists){
            throw new IllegalStateException("You have already sent a friend request to this user.");
        }

        Optional<Friend> receivedRequest = friendRepository
                .findBySenderAndReceiverAndStatus(currentAuthUser, friendship.getReceiver(), FriendStatus.REQUESTED);

        if(receivedRequest.isPresent()){
            Friend existing = receivedRequest.get();

            existing.setStatus(FriendStatus.ACCEPTED);
            return friendToDtoMapper.toDto(friendRepository.save(existing));
        }

        friendship.setSender(currentAuthUser);

        return friendToDtoMapper.toDto(friendRepository.save(friendship));
    }

    @Override
    public FriendResponseDto updateFriendRequest(Long id, FriendStatus status) {
        if(status.equals(FriendStatus.REQUESTED)){
            throw new IllegalArgumentException("Invalid status update: Cannot set to REQUESTED again.");
        }

        Friend friendship = friendRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Friend.class, "id", id));

        UserEntity currentAuthUser= authUtility.getCurrentUser();

        if(status.equals(FriendStatus.ACCEPTED) &&
                !friendship.getReceiver().getId().equals(currentAuthUser.getId())){
            throw new ForbiddenActionException(Friend.class);
        }
        if(status.equals(FriendStatus.DECLINED) &&
                !friendship.getReceiver().getId().equals(currentAuthUser.getId()) &&
                !friendship.getSender().getId().equals(currentAuthUser.getId())){
//            throw new IllegalStateException("Not your friendship to decline.");
            throw new ForbiddenActionException(Friend.class);
        }

        friendship.setStatus(status);
        return friendToDtoMapper.toDto(friendRepository.save(friendship));
    }

    @Override
    public List<FriendResponseDto> getFriends() {
        UserEntity currentAuthUser= authUtility.getCurrentUser();

        return friendToDtoMapper.toDto(
            friendRepository
                    .findBySenderOrReceiverAndStatus(currentAuthUser, currentAuthUser, FriendStatus.ACCEPTED)
        );
    }

    @Override
    public List<FriendResponseDto> getSentPendingRequests() {
        UserEntity currentAuthUser= authUtility.getCurrentUser();

        return friendToDtoMapper.toDto(
                friendRepository
                        .findBySenderAndStatus(currentAuthUser, FriendStatus.REQUESTED)
        );
    }

    @Override
    public List<FriendResponseDto> getReceivedPendingRequests() {
        UserEntity currentAuthUser= authUtility.getCurrentUser();

        return friendToDtoMapper.toDto(
                friendRepository
                        .findByReceiverAndStatus(currentAuthUser, FriendStatus.REQUESTED)
        );
    }
}
