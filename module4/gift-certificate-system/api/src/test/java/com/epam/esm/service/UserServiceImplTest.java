package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.model.User;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {


    @Autowired
    private UserService userService;

    @Test
    void shouldGetAllUsers() {
        List<UserDTO> userList = userService.getAllUsers(1L, 20L);
        assertEquals(5, userList.size());
    }

    @Test
    void shouldFindUserById() {
        UserDTO user1 = userService.getUserById(1L);
        UserDTO user2 = userService.getUserById(2L);
        assertEquals(1L, user1.getId());
        assertEquals(2L, user2.getId());
    }
}