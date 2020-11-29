package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.model.User;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers(Long page, Long pageSize);
    UserDTO getUserById(Long id);
}
