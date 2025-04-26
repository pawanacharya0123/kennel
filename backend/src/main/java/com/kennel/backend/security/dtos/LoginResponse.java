package com.kennel.backend.security.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private  String accessToken;
    private final String refreshToken;

}
