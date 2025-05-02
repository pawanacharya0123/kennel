package com.kennel.backend.dto.userEntity.response;

import com.kennel.backend.entity.Dog;
import com.kennel.backend.entity.Role;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Builder
@Getter
public class UserDetailsResponseDto {
    @NotNull
    private String email;
    private Set<Role> roles;
    @Nullable
    private String extraInfo;
}
