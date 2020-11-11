package com.epam.esm.controller;

import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gift-certificates")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/orders")
    public List<Order> findAllOrders(@RequestParam(value = "from") Optional<Integer> from,
                                     @RequestParam(value = "page_size") Optional<Integer> pages) {
        int fromUOrder = from.orElse(0);
        int pageSize = pages.orElse(20);
        return orderService.getAllOrders(fromUOrder, pageSize);
    }

    @GetMapping(value = "/orders/{id}")
    public Order findOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping(value = "/orders")
    public ResponseEntity<Order> makeOrder(@RequestParam(value = "userId") Optional<Long> userId,
                                                     @RequestParam(value = "certificateId") Optional<Long> certificateId) {

        Long userIdFromRequest = userId.orElseThrow(() -> new InvalidInputDataException("Missing value for the userId parameter"));
        Long certificateIdFromRequest = certificateId.orElseThrow(() -> new InvalidInputDataException("Missing value for the certificateId parameter"));
        orderService.makeOrder(userIdFromRequest, certificateIdFromRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
