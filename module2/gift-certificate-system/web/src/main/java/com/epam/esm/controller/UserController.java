package com.epam.esm.controller;

import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gift-certificates")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public List<User> findAllUsers(@RequestParam(value = "from") Optional<Integer> from,
                                   @RequestParam(value = "page_size") Optional<Integer> pages) {
        int fromUser = from.orElse(0);
        int pageSize = pages.orElse(20);
        return userService.getAllUsers(fromUser, pageSize);
    }

    @GetMapping(value = "/users/{id}")
    public User findUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

}
