package com.epam.esm.service;

import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.model.User;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return userRepository.findAll();
    }

    /**
     * Gets user by id.
     *
     * @param id User id.
     * @return User instance.
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(MessageFormat
                .format("User with id: {0} not found", id)));
    }
}
