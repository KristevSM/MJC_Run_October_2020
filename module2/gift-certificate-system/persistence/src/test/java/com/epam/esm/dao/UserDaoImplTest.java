package com.epam.esm.dao;

import com.epam.esm.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class UserDaoImplTest {

//    private UserDao userDao;
//
//    @BeforeEach
//    void setUp() {
//        userDao = new UserDaoImpl();
//    }
//
//
//    @Test
//    public void shouldFindUserById() {
//        Optional<User> user1 = userDao.find(1L);
//        Optional<User> user2 = userDao.find(22L);
//        Optional<User> user3 = userDao.find(50L);
//        Optional<User> userNotExists = userDao.find(150L);
//        assertTrue(user1.isPresent());
//        assertTrue(user2.isPresent());
//        assertTrue(user3.isPresent());
//        assertFalse(userNotExists.isPresent());
//    }
//
//    @Test
//    public void shouldGetUsersList() {
//
//        List<User> userList = userDao.findAll(1L, 200L);
//        assertEquals(50L, userList.size());
//    }
//
//    @Test
//    public void shouldPaginateUsersList() {
//
//        List<User> userList1 = userDao.findAll(1L, 20L);
//        assertEquals(20L, userList1.size());
//        List<User> userList2 = userDao.findAll(2L, 20L);
//        assertEquals(20L, userList2.size());
//        List<User> userList3 = userDao.findAll(1L, 30L);
//        assertEquals(30L, userList3.size());
//    }
}