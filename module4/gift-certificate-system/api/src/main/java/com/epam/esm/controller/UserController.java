package com.epam.esm.controller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public CollectionModel<UserDTO> findAllUsers(@RequestParam(value = "page") Optional<Integer> page,
                                              @RequestParam(value = "page_size") Optional<Integer> pageSize) {
        int pageNumber = page.orElse(DEFAULT_PAGE_NUMBER);
        int pageSizeNumber = pageSize.orElse(DEFAULT_PAGE_SIZE);

        ValidationUtils.checkPaginationData(pageNumber, pageSizeNumber);

        List<UserDTO> userDTOList = userService.getAllUsers(pageNumber-1, pageSizeNumber);
//        long totalCount = userService.findUsersTotalCount();
//        double totalPages = Math.ceil((double) totalCount / (double) pageSizeNumber);

        for (UserDTO userDTO : userDTOList) {
            Link selfLink = linkTo(methodOn(UserController.class)
                    .findUserById(userDTO.getId())).withRel("currentUser");
            userDTO.add(selfLink);
        }

        Link usersLink = linkTo(UserController.class).slash("users").withRel("usersList");

        CollectionModel<UserDTO> collectionModel = new CollectionModel(userDTOList, usersLink);
        if (pageNumber > 1) {
            Link previousPage = linkTo(methodOn(UserController.class)
                    .findAllUsers(Optional.of(pageNumber - 1), Optional.of(pageSizeNumber))).withRel("previousPage");
            collectionModel.add(previousPage);
        }
//        if (pageNumber < totalPages) {
//            Link nextPage = linkTo(methodOn(UserController.class)
//                    .findAllUsers(Optional.of(pageNumber + 1), Optional.of(pageSizeNumber))).withRel("nextPage");
//            collectionModel.add(nextPage);
//        }
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
