package com.kennel.backend.security.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginRequest {
    private String email;
    private String password;

//    public String getEmail() {
//        return email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
}
