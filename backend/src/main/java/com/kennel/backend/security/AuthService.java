package com.kennel.backend.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.security.dtos.LoginResponse;
import com.kennel.backend.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final JwtDecoder jwtDecoder;
    private final UserEntityService userEntityService;

    public LoginResponse attemptLogin(String email, String password){
        var authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal= (UserPrincipal)authentication.getPrincipal();

        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        var accessToken = jwtIssuer.issuer(principal.getUserId(), principal.getEmail(), roles);
        var refreshToken= jwtIssuer.issueRefreshToken(principal.getUserId(), principal.getEmail());
        return LoginResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public String tokenFromRefreshToken(String refreshToken){
        DecodedJWT decodedJWT = jwtDecoder.decode(refreshToken);

        String email = decodedJWT.getSubject();
        Long userId = decodedJWT.getClaim("userId").asLong();
        UserEntity userEntity = userEntityService
                .getUserEntityByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

//        List<String> roles = Arrays.stream(userEntity.getRole().split(","))
//                    .map(String::trim)
//                    .map(SimpleGrantedAuthority::new)
//                    .map(grantedAuthority -> grantedAuthority.getAuthority())
//                    .toList();

        List<String> roles= userEntity.getRoles()
                .stream()
                .map(role-> role.getRoleName())
                .map(Enum::toString)
                .map(SimpleGrantedAuthority::new)
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();
        return jwtIssuer.issuer(userId, email, roles);
    }
}
