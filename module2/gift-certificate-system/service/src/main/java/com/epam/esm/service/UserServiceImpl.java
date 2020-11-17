package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers(Long page, Long pageSize) {
        return userDao.findAll(page, pageSize);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.find(id).orElseThrow(() -> new UserNotFoundException(MessageFormat
                .format("User with id: {0} not found", id)));
    }

}
