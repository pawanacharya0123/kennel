package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.kennel.KennelDtoMapper;
import com.kennel.backend.dto.kennel.request.KennelCreateRequestDto;
import com.kennel.backend.dto.kennel.request.KennelUpdateRequestDto;
import com.kennel.backend.dto.kennel.response.KennelResponseDto;
import com.kennel.backend.entity.Comment;
import com.kennel.backend.entity.Kennel;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.exception.ForbiddenActionException;
import com.kennel.backend.protection.customAnnotation.EnableSoftDeleteFilter;
import com.kennel.backend.repository.KennelRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.KennelService;
import com.kennel.backend.utility.SlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KennelServiceImpl implements KennelService {
    private final KennelRepository kennelRepository;
    private final UserEntityRepository userEntityRepository;
    private final KennelDtoMapper kennelDtoMapper;
    private final AuthUtility authUtility;

    @Override
    public KennelResponseDto createKennel(KennelCreateRequestDto kennelCreateRequestDto) {
        UserEntity currentAuthUser= authUtility.getCurrentUser();

        Kennel kennel = kennelDtoMapper.toEntity(kennelCreateRequestDto);

        validateKennelNameUnique(kennel.getName(), kennel.getLocation());

        String initialSlug = SlugGenerator.toSlug(kennel.getName() + "-" + kennel.getLocation());
        String finalSlug = ensureUniqueKennelSlug(initialSlug);

        kennel.setOwner(currentAuthUser);
        kennel.setSlug(finalSlug);

        return kennelDtoMapper.toDto(kennelRepository.save(kennel));
    }

    @Override
    @EnableSoftDeleteFilter
    public KennelResponseDto updateKennel(String slug, KennelUpdateRequestDto kennelUpdateRequestDto) {
        Kennel kennel = kennelRepository.findBySlug(slug)
//                .findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new EntityNotFoundException(Kennel.class, "slug", slug));

        checkAccess(kennel);

        Kennel updatedKennelRequest = kennelDtoMapper.toEntity(kennelUpdateRequestDto);

        Kennel updatedKennel = kennel.toBuilder()
                .name(updatedKennelRequest.getName())
                .location(updatedKennelRequest.getLocation())
                .establishedAt(updatedKennelRequest.getEstablishedAt())
                .build();

        validateKennelNameUnique(updatedKennel.getName(), updatedKennel.getLocation());

        return kennelDtoMapper.toDto(kennelRepository.save(updatedKennel));
    }

    @Override
    @EnableSoftDeleteFilter
    public KennelResponseDto getKennelById(Long kennelId) {
        Kennel kennel = kennelRepository.findById(kennelId)
//                .findByIdAndDeletedFalse(kennelId)
                .orElseThrow(() -> new EntityNotFoundException(Kennel.class, "id", kennelId));
        return kennelDtoMapper.toDto(kennel);
    }

    @Override
    @EnableSoftDeleteFilter
    public Page<KennelResponseDto> getAllKennels(Pageable pageable) {
        Page<Kennel> kennels = kennelRepository.findAll(pageable);
//                .findByDeletedFalse(pageable);
        return kennelDtoMapper.toDto(kennels);
    }

    @Override
    @EnableSoftDeleteFilter
    public Page<KennelResponseDto> getKennelsByOwner(Long ownerId, Pageable pageable) {
        UserEntity userEntity = userEntityRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, "id", ownerId));
        if(!userEntity.getKennels().isEmpty()){
            return Page.empty();
        }
        Page<Kennel> kennels = kennelRepository.findByOwnerId(ownerId, pageable);
//                .findByOwnerIdAndDeletedFalse(ownerId, pageable);
        return kennelDtoMapper.toDto(kennels);
    }

    @Override
    @EnableSoftDeleteFilter
    public void deleteKennel(String slug) {
        Kennel kennel = kennelRepository.findBySlug(slug)
//                .findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new EntityNotFoundException(Kennel.class, "slug", slug));

        checkAccess(kennel);

        kennelRepository.delete(kennel);
    }

    @Override
    @EnableSoftDeleteFilter
    public KennelResponseDto getKennelBySlug(String slug) {
        Kennel kennel = kennelRepository.findBySlug(slug)
//                .findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new EntityNotFoundException(Kennel.class, "slug", slug));
        return kennelDtoMapper.toDto(kennel);
    }

    private void checkAccess(Kennel kennel){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!kennel.getOwner().getEmail().equals(userEmail)){
            throw new ForbiddenActionException(Comment.class);
        }
    }

    @EnableSoftDeleteFilter
    private void validateKennelNameUnique(String name, String location){
        boolean exists = kennelRepository.existsByNameAndLocation(name, location);
        if (exists) {
            throw new IllegalStateException("Kennel with name '" + name + "' already exists for this owner.");
        }
    }

    @EnableSoftDeleteFilter
    private String ensureUniqueKennelSlug(String baseSlug) {
        String slug = baseSlug;
        int counter = 1;
        while (kennelRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }
}
