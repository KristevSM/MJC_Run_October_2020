package com.epam.esm.service;

import com.epam.esm.controller.UserController;
import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    /**
     * Gets list of all users from UserDao.
     *
     * @param page     Index of first page instance.
     * @param pageSize Count pages in response.
     * @return Users list.
     */
    @Override
    public List<UserDTO> getAllUsers(Long page, Long pageSize) {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(userConverter::convertUserDTOFromUser)
                .collect(Collectors.toList());
    }

    /**
     * Gets user by id.
     *
     * @param id User id.
     * @return User instance.
     */
    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(MessageFormat
                .format("User with id: {0} not found", id)));
        return userConverter.convertUserDTOFromUser(user);
    }
}
