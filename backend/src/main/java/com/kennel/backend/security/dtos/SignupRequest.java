package com.kennel.backend.security.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class SignupRequest {
    private String email;
    private String password;
    private String role;

}
