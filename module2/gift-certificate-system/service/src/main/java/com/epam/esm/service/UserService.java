package com.epam.esm.service;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(int from, int pageSize);
    User getUserById(Long id);
}
