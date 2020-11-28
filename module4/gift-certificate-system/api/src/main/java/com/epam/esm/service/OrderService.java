package com.epam.esm.service;

import com.epam.esm.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders(Long page, Long pageSize);
    Order getOrderById(Long id);
    Order makeOrder(Long userId, Long certificateId);
    void removeOrder(Long orderId);
    List<Order> getUserOrders(Long userId, Long page, Long pageSize);
    Long findOrderTotalCountByUserId(Long userId);
    Long findOrderTotalCount();

}
