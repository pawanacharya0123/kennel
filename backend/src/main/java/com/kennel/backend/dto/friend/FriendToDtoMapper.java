package com.kennel.backend.dto.friend;

import com.kennel.backend.dto.friend.request.FriendRequestDto;
import com.kennel.backend.dto.friend.response.FriendResponseDto;
import com.kennel.backend.dto.userEntity.UserEntityDtoMapper;
import com.kennel.backend.entity.Friend;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendToDtoMapper {
    private final UserEntityRepository userEntityRepository;
    private final UserEntityDtoMapper userEntityDtoMapper;

    public Friend toEntity(FriendRequestDto friendRequestDto){
        UserEntity receiver= userEntityRepository.findById(friendRequestDto.getReceiver())
                .orElseThrow(()-> new EntityNotFoundException(UserEntity.class, "id", friendRequestDto.getReceiver()));

        return Friend.builder()
                .status(friendRequestDto.getStatus())
                .receiver(receiver)
                .build();
    }

    public FriendResponseDto toDto(Friend friend){
        return FriendResponseDto.builder()
                .from(userEntityDtoMapper.toDto(friend.getSender()))
                .to(userEntityDtoMapper.toDto(friend.getSender()))
                .status(friend.getStatus())
                .build();
    }

    public List<FriendResponseDto> toDto(List<Friend> friends){
        return friends.stream().map(this::toDto).toList();
    }

    public Page<FriendResponseDto> toDto(Page<Friend> friends){
        return friends.map(this::toDto);
    }
}
