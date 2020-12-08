package com.epam.esm.service;

import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserConverter userConverter;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserConverter userConverter, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Gets list of all users from UserDao.
     *
     * @param page     Index of first page instance.
     * @param pageSize Count pages in response.
     * @return Users list.
     */
    @Override
    public Page<UserDTO> getAllUsers(int page, int pageSize) {
        return userRepository.findAll(PageRequest.of(page, pageSize))
                        .map(userConverter::convertFromEntity);
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
        return userConverter.convertFromEntity(user);
    }

    @Override
    public UserDTO register(User user) {
        Optional<User> userOptional = userRepository.findUserByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            throw new IllegalArgumentException(MessageFormat.format("User with username: {0} already exists", user.getUsername()));
        }
        Role roleUser = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new IllegalArgumentException(MessageFormat
                .format("Role with name: {0} not found", "ROLE_USER")));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleUser);
        user.setRoles(userRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);
        log.info("IN register - user: {} successfully registered", registeredUser);
        return userConverter.convertFromEntity(registeredUser);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(MessageFormat
                .format("User with username: {0} not found", username)));
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        return userRepository.findUserByUsernameAndPassword(username, encodedPassword).orElseThrow(() -> new UserNotFoundException(MessageFormat
                .format("User with username: {0} not found", username)));

    }
}
