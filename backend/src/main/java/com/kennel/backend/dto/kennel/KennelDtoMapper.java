package com.kennel.backend.dto.kennel;

import com.kennel.backend.dto.kennel.request.KennelRequestDto;
import com.kennel.backend.dto.kennel.response.KennelResponseDto;
import com.kennel.backend.dto.userEntity.UserEntityDtoMapper;
import com.kennel.backend.entity.Kennel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KennelDtoMapper {
    private final UserEntityDtoMapper userEntityDtoMapper;

    public Kennel toEntity(KennelRequestDto kennelRequestDto){
        return Kennel.builder()
                .name(kennelRequestDto.getName())
                .establishedAt(kennelRequestDto.getEstablishedAt())
                .location(kennelRequestDto.getLocation())
                .build();
    }

    public KennelResponseDto toDto(Kennel kennel){
        return KennelResponseDto.builder()
                .slug(kennel.getSlug())
                .name(kennel.getName())
                .location(kennel.getLocation())
                .establishedAt(kennel.getEstablishedAt())
                .owner(userEntityDtoMapper.toDto(kennel.getOwner()))
                .build();
    }

    public List<KennelResponseDto> toDto(List<Kennel> kennels){
        return kennels.stream().map(this::toDto).toList();
    }
    public Page<KennelResponseDto> toDto(Page<Kennel> kennels){
        return kennels.map(this::toDto);
    }
}
