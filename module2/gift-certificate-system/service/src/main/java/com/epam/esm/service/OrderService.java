package com.epam.esm.service;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders(int from, int pageSize);
    Order getOrderById(Long id);
    void makeOrder(Long userId, Long certificateId);

}
