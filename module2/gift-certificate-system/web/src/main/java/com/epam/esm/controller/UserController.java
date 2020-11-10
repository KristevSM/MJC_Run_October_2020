package com.epam.esm.controller;

import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
