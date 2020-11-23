package com.epam.esm.service;

import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers(Long page, Long pageSize);
    User getUserById(Long id);
    Long findUsersTotalCount();
}
