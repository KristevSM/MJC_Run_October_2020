package com.epam.esm.service;

import com.epam.esm.model.User;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers(int from, int pageSize);
}
