package com.kennel.backend.service;

import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserEntityService {
    private final UserEntityRepository userEntityRepository;


    public UserEntity registerUser(UserEntity userEntity){
        return userEntityRepository.save(userEntity);
    }
    public Optional<UserEntity> getUserEntityByEmail(String email) {
        return userEntityRepository.findByEmail(email);

    }
}
