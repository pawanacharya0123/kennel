package com.kennel.backend.security;

import com.kennel.backend.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserEntityService userEntityService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user= userEntityService.getUserEntityByEmail(username).orElseThrow();

        return UserPrincipal.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .authorities(
                        Arrays.stream(user.getRole().split(","))
                                .map(String::trim)
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())
                )
                .password(user.getPassword())
                .build();
    }
}
