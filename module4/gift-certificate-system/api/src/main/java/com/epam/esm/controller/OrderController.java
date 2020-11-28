package com.epam.esm.controller;

import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.model.Order;
import com.epam.esm.service.OrderService;
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
        long pageNumber = page.orElse(DEFAULT_PAGE_NUMBER);
        long pageSizeNumber = pageSize.orElse(DEFAULT_PAGE_SIZE);
        ValidationUtils.checkPaginationData(pageNumber, pageSizeNumber);

        List<Order> orderList = orderService.getAllOrders(pageNumber, pageSizeNumber);
//        long totalCount = orderService.findOrderTotalCount();
//        double totalPages = Math.ceil((double) totalCount / (double) pageSizeNumber);
        for (Order order : orderList) {
            Link selfLink = linkTo(methodOn(OrderController.class)
                    .findOrderById(order.getId())).withRel("currentOrder");
            order.add(selfLink);
        }

        Link orders = linkTo(OrderController.class).slash("orders").withRel("ordersList");

        CollectionModel<Order> collectionModel = new CollectionModel(orderList, orders);
        if (pageNumber > 1) {
            Link previousPage = linkTo(methodOn(OrderController.class)
                    .findAllOrders(Optional.of(pageNumber - 1), Optional.of(pageSizeNumber))).withRel("previousPage");
            collectionModel.add(previousPage);
        }
//        if (pageNumber < totalPages) {
//            Link nextPage = linkTo(methodOn(OrderController.class)
//                    .findAllOrders(Optional.of(pageNumber + 1), Optional.of(pageSizeNumber))).withRel("nextPage");
//            collectionModel.add(nextPage);
//        }
        return collectionModel;
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
        return addHateoasLinksToOrder(order);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/orders")
    public Order makeOrder(@RequestParam(value = "user_id") Optional<Long> userId,
                           @RequestParam(value = "certificate_id") Optional<Long> certificateId) {

        long userIdFromRequest = userId.orElseThrow(() -> new InvalidInputDataException("Missing value for the userId parameter"));
        long certificateIdFromRequest = certificateId.orElseThrow(() -> new InvalidInputDataException("Missing value for the certificateId parameter"));
        Order order = orderService.makeOrder(userIdFromRequest, certificateIdFromRequest);
        return addHateoasLinksToOrder(order);
    }

    private Order addHateoasLinksToOrder(Order order) {
        Link selfLink = linkTo(methodOn(OrderController.class)
                .findOrderById(order.getId())).withRel("currentOrder");
        Link ordersLink = linkTo(methodOn(OrderController.class)
                .findAllOrders(Optional.of(DEFAULT_PAGE_NUMBER), Optional.of(DEFAULT_PAGE_SIZE))).withRel("ordersList");
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
    public CollectionModel<Order> getUserOrders(@PathVariable Long id,
                                                @RequestParam(value = "page") Optional<Long> page,
                                                @RequestParam(value = "page_size") Optional<Long> pageSize
    ) {
        long pageNumber = page.orElse(DEFAULT_PAGE_NUMBER);
        long pageSizeNumber = pageSize.orElse(DEFAULT_PAGE_SIZE);

        ValidationUtils.checkPaginationData(pageNumber, pageSizeNumber);
        List<Order> orderList = orderService.getUserOrders(id, pageNumber, pageSizeNumber);
//        long totalCount = orderService.findOrderTotalCountByUserId(id);
//        double totalPages = Math.ceil((double) totalCount / (double) pageSizeNumber);
        for (Order order : orderList) {
            Link selfLink = linkTo(methodOn(OrderController.class)
                    .findOrderById(order.getId())).withRel("order");
            order.add(selfLink);
        }

        CollectionModel<Order> collectionModel = new CollectionModel(orderList);
        if (pageNumber > 1) {
            Link previousPage = linkTo(methodOn(OrderController.class)
                    .getUserOrders(id, Optional.of(pageNumber - 1), Optional.of(pageSizeNumber))).withRel("previousPage");
            collectionModel.add(previousPage);
        }
//        if (pageNumber < totalPages) {
//            Link nextPage = linkTo(methodOn(OrderController.class)
//                    .getUserOrders(id, Optional.of(pageNumber + 1), Optional.of(pageSizeNumber))).withRel("nextPage");
//            collectionModel.add(nextPage);
//        }
        return collectionModel;
    }
}
