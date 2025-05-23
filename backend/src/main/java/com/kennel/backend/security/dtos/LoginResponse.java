package com.kennel.backend.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {
    private  String accessToken;
    private final String refreshToken;

}
