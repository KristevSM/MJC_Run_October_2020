package com.epam.esm.service;

import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.model.Role;
import com.epam.esm.model.Tag;
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
import java.util.stream.Collectors;

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
    public List<UserDTO> getAllUsers(int page, int pageSize) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, pageSize));
        List<User> userList = userPage.toList();
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

    @Override
    public UserDTO register(User user) {
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            throw new IllegalArgumentException(MessageFormat.format("User with same email: {0} already exists", user.getEmail()));
        }
        Role roleUser = roleRepository.findByName("USER_ROLE").orElseThrow(() -> new IllegalArgumentException(MessageFormat
                .format("Role with name: {0} not found", "USER_ROLE")));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleUser);
        user.setRoles(userRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);
        log.info("IN register - user: {} successfully registered", registeredUser);
        return userConverter.convertUserDTOFromUser(registeredUser);
    }
}
