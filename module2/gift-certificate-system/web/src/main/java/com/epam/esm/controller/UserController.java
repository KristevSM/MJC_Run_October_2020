package com.epam.esm.controller;

import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.constants.AppConstants.DEFAULT_PAGE_NUMBER;
import static com.epam.esm.constants.AppConstants.DEFAULT_PAGE_SIZE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Sergei Kristev
 * <p>
 * Gets data from rest in JSON format on path "/gift-certificates".
 */
@RestController
@RequestMapping("/gift-certificates")
public class UserController {

    private final UserService userService;

    /**
     * Constructor accepts service layer object.
     *
     * @param userService UserService instance.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get users list.
     * <p>
     * Getting users list.
     *
     * @param page  page's number
     * @param pageSize page size
     * @return User list.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users", produces = {"application/hal+json"})
    public CollectionModel<User> findAllUsers(@RequestParam(value = "page") Optional<Long> page,
                                              @RequestParam(value = "page_size") Optional<Long> pageSize) {
        Long pageNumber = page.orElse(DEFAULT_PAGE_NUMBER);
        Long pageSizeNumber = pageSize.orElse(DEFAULT_PAGE_SIZE);

        ValidationUtils.checkPaginationData(pageNumber, pageSizeNumber);

        List<User> userList = userService.getAllUsers(pageNumber, pageSizeNumber);
        for (User user : userList) {
            Link selfLink = linkTo(methodOn(UserController.class)
                    .findUserById(user.getId())).withSelfRel();
            user.add(selfLink);
            Link ordersLink = linkTo(methodOn(OrderController.class)
                    .getUserOrders(user.getId(), Optional.of(DEFAULT_PAGE_NUMBER), Optional.of(DEFAULT_PAGE_SIZE))).withRel("usersOrders");
            user.add(ordersLink);
        }
        Link link = linkTo(UserController.class).slash("users").withSelfRel();
        return new CollectionModel<>(userList, link);
    }

    /**
     * Gets user by id.
     *
     * @param id User id.
     * @return User instance.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users/{id}", produces = {"application/hal+json"})
    public User findUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        Link selfLink = linkTo(methodOn(UserController.class)
                .findUserById(user.getId())).withSelfRel();
        Link ordersLink = linkTo(methodOn(OrderController.class)
                .getUserOrders(user.getId(), Optional.of(DEFAULT_PAGE_NUMBER), Optional.of(DEFAULT_PAGE_SIZE))).withRel("usersOrders");
        user.add(ordersLink);
        user.add(selfLink);
        return user;
    }

}
