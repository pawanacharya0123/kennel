package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.kennel.KannelDtoMapper;
import com.kennel.backend.dto.kennel.request.KennelCreateRequestDto;
import com.kennel.backend.dto.kennel.request.KennelRequestDto;
import com.kennel.backend.dto.kennel.request.KennelUpdateRequestDto;
import com.kennel.backend.dto.kennel.response.KennelResponseDto;
import com.kennel.backend.entity.Kennel;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.repository.KennelRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.service.KennelService;
import com.kennel.backend.utility.SlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KennelServiceImpl implements KennelService {
    private final KennelRepository kennelRepository;
    private final UserEntityRepository userEntityRepository;
    private final KannelDtoMapper kannelDtoMapper;

    @Override
    public KennelResponseDto createKennel(KennelCreateRequestDto kennelCreateRequestDto, Long ownerId) {
        UserEntity user =userEntityRepository.findById(ownerId)
                .orElseThrow(()-> new EntityNotFoundException(UserEntity.class, "id", ownerId));

        Kennel kennel = kannelDtoMapper.toEntity(kennelCreateRequestDto);

        String initialSlug = SlugGenerator.toSlug(kennel.getName() + "-" + kennel.getLocation());
        String finalSlug = ensureUniqueDogSlug(initialSlug);

        kennel.setOwner(user);
        kennel.setSlug(finalSlug);

        validateDogNameUnique(kennel.getName(), kennel.getLocation());

        return kannelDtoMapper.toDto(kennelRepository.save(kennel));
    }

    @Override
    public KennelResponseDto updateKennel(String slug, KennelUpdateRequestDto kennelUpdateRequestDto) {
        Kennel kennel = kennelRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Kennel.class, "slug", slug));

        Kennel updatedKennelRequest = kannelDtoMapper.toEntity(kennelUpdateRequestDto);

        Kennel updatedKennel = kennel.toBuilder()
                .name(updatedKennelRequest.getName())
                .location(updatedKennelRequest.getLocation())
                .establishedAt(updatedKennelRequest.getEstablishedAt())
                .build();

        validateDogNameUnique(updatedKennel.getName(), updatedKennel.getLocation());

        return kannelDtoMapper.toDto(kennelRepository.save(updatedKennel));
    }

    @Override
    public KennelResponseDto getKennelById(Long kennelId) {
        Kennel kennel = kennelRepository.findById(kennelId)
                .orElseThrow(() -> new EntityNotFoundException(Kennel.class, "id", kennelId));
        return kannelDtoMapper.toDto(kennel);
    }

    @Override
    public List<KennelResponseDto> getAllKennels() {
        List<Kennel> kennels = kennelRepository.findAll();
        return kannelDtoMapper.toDto(kennels);
    }

    @Override
    public List<KennelResponseDto> getKennelsByOwner(Long ownerId) {
        List<Kennel> kennels = kennelRepository.findByOwnerId(ownerId);
        return kannelDtoMapper.toDto(kennels);
    }

    @Override
    public void deleteKennel(String slug) {
        kennelRepository.deleteBySlug(slug);
    }

    @Override
    public KennelResponseDto getKennelBySlug(String slug) {
        Kennel kennel = kennelRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Kennel.class, "slug", slug));
        return kannelDtoMapper.toDto(kennel);
    }

    private void validateDogNameUnique(String name, String location){
        boolean exists = kennelRepository.existsByNameAndLocation(name, location);
        if (exists) {
            throw new IllegalStateException("Dog with name '" + name + "' already exists for this owner.");
        }
    }

    private String ensureUniqueDogSlug(String baseSlug) {
        String slug = baseSlug;
        int counter = 1;
        while (kennelRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }
}
