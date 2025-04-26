package com.kennel.backend.service;

import com.kennel.backend.entity.Role;
import com.kennel.backend.entity.enums.RoleName;
import com.kennel.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Optional<Role> findRoleByName(RoleName roleName){
        return roleRepository.findByRoleName(roleName);
    }
    public Role saveRole(Role role){
        return roleRepository.save(role);
    }
}
