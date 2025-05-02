package com.kennel.backend.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtToPrincipalConvertor {

    public UserPrincipal convert(DecodedJWT decodedJWT){
        return UserPrincipal.builder()
                .userId(Long.valueOf(decodedJWT.getSubject()))
                .email(decodedJWT.getClaim("email").asString())
                .authorities(extractAuthoritiesFromClaim(decodedJWT))
                .build();
    }

    private List<SimpleGrantedAuthority> extractAuthoritiesFromClaim(DecodedJWT decodedJWT){
        var claim= decodedJWT.getClaim("roles");

        if(claim.isNull() || claim.isMissing()) return List.of();

        return claim.asList(String.class)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
