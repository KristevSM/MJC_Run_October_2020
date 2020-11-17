package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders(Long page, Long pageSize);
    Order getOrderById(Long id);
    void makeOrder(Long userId, Long certificateId);
    void removeOrder(Long orderId);
    OrderDto getOrderDetails(Long userId, Long orderId);
    List<OrderDto> getUserOrders(Long userId, Long page, Long pageSize);

}
