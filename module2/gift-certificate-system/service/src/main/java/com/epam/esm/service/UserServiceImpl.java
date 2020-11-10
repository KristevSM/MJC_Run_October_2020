package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers(int from, int pageSize) {
        //validate
        return userDao.getAllUsers(from, pageSize);
    }
}
