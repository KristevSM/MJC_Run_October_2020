package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.model.User;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers(int page, int pageSize);
    UserDTO getUserById(Long id);
    UserDTO register(User user);
}
