package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PaginationUtil {
    private static final String NEXT_PAGE = "nextPage";
    private static final String PREV_PAGE = "prevPage";
    private static final String FIRST_PAGE = "firstPage";
    private static final String LAST_PAGE = "lastPage";


    void addPaginationLinksToOrderDTO(
            Long userId,
            CollectionModel<OrderDTO> orderDTOS,
            final int pageNumber,
            final int totalPages,
            final int pageSize) {

        if (hasNextPage(pageNumber, totalPages)) {
            orderDTOS.add(getLinkToFindUserOrdersMethod(userId, pageNumber + 1, pageSize, NEXT_PAGE));
        }
        if (hasPreviousPage(pageNumber)) {
            orderDTOS.add(getLinkToFindUserOrdersMethod(userId, pageNumber - 1, pageSize, PREV_PAGE));
        }
        if (hasFirstPage(pageNumber)) {
            orderDTOS.add(getLinkToFindUserOrdersMethod(userId, 1, pageSize, FIRST_PAGE));
        }
        if (hasLastPage(pageNumber, totalPages)) {
            orderDTOS.add(getLinkToFindUserOrdersMethod(userId, totalPages - 1, pageSize, LAST_PAGE));
        }
    }

    void addPaginationLinksToUserDTO(
            CollectionModel<UserDTO> userDTOS,
            final int pageNumber,
            final int totalPages,
            final int pageSize) {

        if (hasNextPage(pageNumber, totalPages)) {
            userDTOS.add(getLinkToFindAllUsersMethod(pageNumber + 1, pageSize, NEXT_PAGE));
        }
        if (hasPreviousPage(pageNumber)) {
            userDTOS.add(getLinkToFindAllUsersMethod(pageNumber - 1, pageSize, PREV_PAGE));
        }
        if (hasFirstPage(pageNumber)) {
            userDTOS.add(getLinkToFindAllUsersMethod(1, pageSize, FIRST_PAGE));
        }
        if (hasLastPage(pageNumber, totalPages)) {
            userDTOS.add(getLinkToFindAllUsersMethod(totalPages - 1, pageSize, LAST_PAGE));
        }
    }

    private Link getLinkToFindUserOrdersMethod(Long id, Integer pageSize, Integer pageNumber, String linkName) {
        return linkTo(methodOn(OrderController.class).getUserOrders(id, Optional.of(pageSize), Optional.of(pageNumber))).withSelfRel().withName(linkName);
    }

    private Link getLinkToFindAllUsersMethod(Integer pageSize, Integer pageNumber, String linkName) {
        return linkTo(methodOn(UserController.class).findAllUsers(Optional.of(pageSize), Optional.of(pageNumber))).withSelfRel().withName(linkName);
    }

    private boolean hasNextPage(final int pageNumber, final int totalPages) {
        return pageNumber < totalPages - 1;
    }

    private boolean hasPreviousPage(final int pageNumber) {
        return pageNumber > 1;
    }

    private boolean hasFirstPage(final int pageNumber) {
        return hasPreviousPage(pageNumber);
    }

    private boolean hasLastPage(final int pageNumber, final int totalPages) {
        return totalPages > 1 && hasNextPage(pageNumber, totalPages);
    }
}
