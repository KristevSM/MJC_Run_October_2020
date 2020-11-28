package com.epam.esm.service;

import com.epam.esm.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(Long page, Long pageSize);
    User getUserById(Long id);
    Long findUsersTotalCount();
}
