package com.epam.esm.converter;

import com.epam.esm.dto.RoleDTO;
import com.epam.esm.model.Role;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleConverter {
    Set<RoleDTO> convertFromEntity(Set<Role> roles) {
        if (roles == null) {
            return new HashSet<>();
        }
        return roles.stream()
                .map(role -> RoleDTO.builder()
                        .name(role.getName())
                        .build())
                .collect(Collectors.toSet());
    }

    Set<Role> convertFromDTO(Set<RoleDTO> roleDTOS) {
        if (roleDTOS == null) {
            return new HashSet<>();
        }
        return roleDTOS.stream()
                .map(roleDTO -> Role.builder()
                        .name(roleDTO.getName())
                        .users(new HashSet<>())
                        .build())
                .collect(Collectors.toSet());
    }
}
