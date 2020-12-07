package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    Page<UserDTO> getAllUsers(int page, int pageSize);
    UserDTO getUserById(Long id);
    UserDTO register(User user);
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}
