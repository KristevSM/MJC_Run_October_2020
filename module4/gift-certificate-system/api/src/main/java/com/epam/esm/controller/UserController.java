package com.epam.esm.controller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final PaginationUtil paginationUtil;

    /**
     * Constructor accepts service layer object.
     *
     * @param userService UserService instance.
     */
    @Autowired
    public UserController(UserService userService, PaginationUtil paginationUtil) {
        this.userService = userService;
        this.paginationUtil = paginationUtil;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CollectionModel<UserDTO> findAllUsers(@RequestParam(value = "page") Optional<Integer> page,
                                              @RequestParam(value = "page_size") Optional<Integer> pageSize) {
        int pageNumber = page.orElse(DEFAULT_PAGE_NUMBER);
        int pageSizeNumber = pageSize.orElse(DEFAULT_PAGE_SIZE);

        ValidationUtils.checkPaginationData(pageNumber, pageSizeNumber);

        Page<UserDTO> userDTOPage = userService.getAllUsers(pageNumber-1, pageSizeNumber);

        for (UserDTO userDTO : userDTOPage) {
            Link selfLink = linkTo(methodOn(UserController.class)
                    .findUserById(userDTO.getId())).withRel("currentUser");
            userDTO.add(selfLink);
        }

        Link usersLink = linkTo(UserController.class).slash("users").withRel("usersList");

        CollectionModel<UserDTO> collectionModel = new CollectionModel(userDTOPage, usersLink);
        paginationUtil.addPaginationLinksToUserDTO(
                collectionModel,
                pageNumber,
                userDTOPage.getTotalPages(),
                pageSizeNumber
        );
        return collectionModel;
    }

    /**
     * Gets user by id.
     *
     * @param id User id.
     * @return User instance.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users/{id}", produces = {"application/hal+json"})
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN') && @authorizationComponentImpl.userHasAccess(principal, #id)")
    public UserDTO findUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        Link selfLink = linkTo(methodOn(UserController.class)
                .findUserById(userDTO.getId())).withRel("currentUser");
        Link ordersLink = linkTo(methodOn(OrderController.class)
                .getUserOrders(userDTO.getId(), Optional.of(DEFAULT_PAGE_NUMBER), Optional.of(DEFAULT_PAGE_SIZE))).withRel("usersOrders");
        userDTO.add(ordersLink);
        userDTO.add(selfLink);
        return userDTO;
    }

}
