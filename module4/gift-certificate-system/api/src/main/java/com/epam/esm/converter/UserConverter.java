package com.epam.esm.converter;

import com.epam.esm.dto.RoleDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class UserConverter {

    private final RoleConverter roleConverter;

    public UserDTO convertFromEntity(User user) {

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
        Set<Role> roles = user.getRoles();
        if (roles != null) {
            userDTO.setRoles(roleConverter.convertFromEntity(roles));
        }
        return userDTO;
    }

    public User convertFromDTO(UserDTO userDTO) {
        User user = User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .dateOfBirth(userDTO.getDateOfBirth())
                .address(userDTO.getAddress())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();
        Set<RoleDTO> roleDTOS = userDTO.getRoles();
        if (roleDTOS != null) {
            user.setRoles(roleConverter.convertFromDTO(roleDTOS));
        }
        return user;
    }
}
