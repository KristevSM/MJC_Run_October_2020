package com.epam.esm.dao;

import com.epam.esm.model.User;

import java.util.List;

public interface UserDao extends CrudDAO<User> {
    List<User> getAllUsers();
}
