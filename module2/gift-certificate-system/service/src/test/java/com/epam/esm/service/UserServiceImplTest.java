package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.UserDaoImpl;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private UserService userService;

    private UserDao userDao;

    @BeforeEach
    void setUp() {
        this.userDao = mock(UserDaoImpl.class);
        this.userService = new UserServiceImpl(userDao);
    }

    @Test
    void shouldReturnAllTags() {

        List<User> users = mock(ArrayList.class);
        when(users.size()).thenReturn(10);
        when(userService.getAllUsers(1L, 20L)).thenReturn(users);

        assertEquals(10, userService.getAllUsers(1L, 20L).size());
        Mockito.verify(userDao, Mockito.times(1)).findAll(1L, 20L);
    }

    @Test
    void shouldFindUserById() {

        User user = mock(User.class);
        when(userDao.find(1L)).thenReturn(Optional.of(user));

        assertEquals(user, userService.getUserById(1L));
        Mockito.verify(userDao, Mockito.times(1)).find(1L);
    }

    @Test
    void shouldThrowExceptionWhenCertificateByIdNotFound() {

        User user = mock(User.class);
        when(userDao.find(2L)).thenReturn(Optional.of(user));

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1L);
        });
        Mockito.verify(userDao, Mockito.times(1)).find(1L);
    }

}