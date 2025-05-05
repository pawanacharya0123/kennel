package com.kennel.backend.service;

import com.kennel.backend.dto.kennel.request.KennelCreateRequestDto;
import com.kennel.backend.dto.kennel.request.KennelRequestDto;
import com.kennel.backend.dto.kennel.request.KennelUpdateRequestDto;
import com.kennel.backend.dto.kennel.response.KennelResponseDto;
import com.kennel.backend.entity.Kennel;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface KennelService {
    KennelResponseDto createKennel(KennelCreateRequestDto kennelCreateRequestDto);
    KennelResponseDto updateKennel(String slug, KennelUpdateRequestDto kennelUpdateRequestDto);
    KennelResponseDto getKennelById(Long kennelId);
    Page<KennelResponseDto> getAllKennels(Pageable pageable);
    Page<KennelResponseDto> getKennelsByOwner(Long ownerId, Pageable pageable);
    void deleteKennel(String slug);
    KennelResponseDto getKennelBySlug(String slug);
}
