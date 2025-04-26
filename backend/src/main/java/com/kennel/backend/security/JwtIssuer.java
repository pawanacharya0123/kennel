package com.kennel.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kennel.backend.pets.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtIssuer {

    private final JwtProperties properties;

    public String issuer(long userId, String email, List<String> roles){
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.ofMinutes(5)))
                .withClaim("email", email)
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC256(properties.getSecretKey()));
    }

    public String issueRefreshToken(Long userId, String email) {
        return JWT.create()
                .withSubject(email)
                .withClaim("userId", userId)
                .withExpiresAt(Instant.now().plus(Duration.ofMinutes(60)))
                .sign(Algorithm.HMAC256(properties.getSecretKey()));
    }
}
