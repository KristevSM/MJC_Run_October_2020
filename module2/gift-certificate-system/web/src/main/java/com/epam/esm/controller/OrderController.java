package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.model.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class OrderController {

    private final OrderService orderService;

    /**
     * Accepts service layer objects.
     *
     * @param orderService OrderService instance.
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Gets list of all orders.
     *
     * @param page     page's number
     * @param pageSize page size
     * @return Orders list.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/orders", produces = {"application/hal+json"})
    public CollectionModel<Order> findAllOrders(@RequestParam(value = "page") Optional<Long> page,
                                                @RequestParam(value = "page_size") Optional<Long> pageSize) {
        Long pageNumber = page.orElse(DEFAULT_PAGE_NUMBER);
        Long pageSizeNumber = pageSize.orElse(DEFAULT_PAGE_SIZE);
        ValidationUtils.checkPaginationData(pageNumber, pageSizeNumber);

        List<Order> orderList = orderService.getAllOrders(pageNumber, pageSizeNumber);
        for (Order order : orderList) {
            Link selfLink = linkTo(methodOn(OrderController.class)
                    .findOrderById(order.getId())).withSelfRel();
            order.add(selfLink);
        }
        Link link = linkTo(OrderController.class).slash("orders").withSelfRel();
        return new CollectionModel<>(orderList, link);
    }

    /**
     * Gets order by id.
     *
     * @param id Order id.
     * @return Order instance.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/orders/{id}", produces = {"application/hal+json"})
    public Order findOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        Link selfLink = linkTo(methodOn(OrderController.class)
                .findOrderById(order.getId())).withSelfRel();
        Link ordersLink = linkTo(methodOn(OrderController.class)
                .findAllOrders(Optional.of(DEFAULT_PAGE_NUMBER), Optional.of(DEFAULT_PAGE_SIZE))).withRel("orders");
        order.add(ordersLink);
        order.add(selfLink);
        return order;
    }

    @PostMapping(value = "/orders")
    public ResponseEntity<Order> makeOrder(@RequestParam(value = "user_id") Optional<Long> userId,
                                           @RequestParam(value = "certificate_id") Optional<Long> certificateId) {

        Long userIdFromRequest = userId.orElseThrow(() -> new InvalidInputDataException("Missing value for the userId parameter"));
        Long certificateIdFromRequest = certificateId.orElseThrow(() -> new InvalidInputDataException("Missing value for the certificateId parameter"));
        orderService.makeOrder(userIdFromRequest, certificateIdFromRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Get user's order details
     *
     * @param userId  User id.
     * @param orderId Order id.
     * @return Order instance.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users/{userId}/orders/{orderId}", produces = {"application/hal+json"})
    public OrderDto getUsersOrderDetails(@PathVariable(value = "userId") Long userId,
                                         @PathVariable(value = "orderId") Long orderId) {
        OrderDto order = orderService.getOrderDetails(userId, orderId);
        Link selfLink = linkTo(methodOn(OrderController.class)
                .findOrderById(order.getId())).withSelfRel();
        Link ordersLink = linkTo(methodOn(OrderController.class)
                .findAllOrders(Optional.of(DEFAULT_PAGE_NUMBER), Optional.of(DEFAULT_PAGE_SIZE))).withRel("orders");
        order.add(ordersLink);
        order.add(selfLink);
        return order;
    }

    /**
     * Get user's order details
     *
     * @param id       User id.
     * @param page     page's number
     * @param pageSize page size
     * @return Order instance.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users/{id}/orders", produces = {"application/hal+json"})
    public CollectionModel<OrderDto> getUserOrders(@PathVariable Long id,
                                                   @RequestParam(value = "page") Optional<Long> page,
                                                   @RequestParam(value = "page_size") Optional<Long> pageSize
    ) {
        long pageNumber = page.orElse(DEFAULT_PAGE_NUMBER);
        long pageSizeNumber = pageSize.orElse(DEFAULT_PAGE_SIZE);

        ValidationUtils.checkPaginationData(pageNumber, pageSizeNumber);
        List<OrderDto> orderDtoList = orderService.getUserOrders(id, pageNumber, pageSizeNumber);
        Long totalCount = orderService.findOrderTotalCountByUserId(id);
        double totalPages = Math.ceil((double)totalCount / (double)pageSizeNumber);
        for (OrderDto order : orderDtoList) {
            Link selfLink = linkTo(methodOn(OrderController.class)
                    .findOrderById(order.getId())).withRel("order");
            order.add(selfLink);
        }

        Link orders = linkTo(OrderController.class).slash("orders").withRel("orders");

        CollectionModel<OrderDto> collectionModel = new CollectionModel(orderDtoList, orders);
        if (pageNumber > 1) {
            Link previousPage = linkTo(methodOn(OrderController.class)
                    .getUserOrders(id, Optional.of(pageNumber - 1), pageSize)).withRel("previousPage");
            collectionModel.add(previousPage);
        }
        if (pageNumber < totalPages) {
            Link nextPage = linkTo(methodOn(OrderController.class)
                    .getUserOrders(id, Optional.of(pageNumber + 1), pageSize)).withRel("nextPage");
            collectionModel.add(nextPage);
        }
        return collectionModel;
    }
}
