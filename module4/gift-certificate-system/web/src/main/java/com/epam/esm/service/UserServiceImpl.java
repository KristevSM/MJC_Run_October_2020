package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author Sergei Kristev
 * <p>
 * Service for managing Users objects.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    /**
     * Constructor accepts UserDao object.
     *
     * @param userDao UserDao instance.
     */
    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Gets list of all users from UserDao.
     *
     * @param page     Index of first page instance.
     * @param pageSize Count pages in response.
     * @return Users list.
     */
    @Override
    public List<User> getAllUsers(Long page, Long pageSize) {
        return userDao.findAll(page, pageSize);
    }

    /**
     * Gets user by id.
     *
     * @param id User id.
     * @return User instance.
     */
    @Override
    public User getUserById(Long id) {
        return userDao.find(id).orElseThrow(() -> new UserNotFoundException(MessageFormat
                .format("User with id: {0} not found", id)));
    }

    /**
     * Gets total count of users in DB.
     *
     * @return Long users count.
     */
    @Override
    public Long findUsersTotalCount() {
        return userDao.findUsersTotalCount();
    }
}
