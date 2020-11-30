package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.model.Order;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrders(int page, int pageSize);
    OrderDTO getOrderById(Long id);
    OrderDTO makeOrder(Long userId, Long certificateId);
    void removeOrder(Long orderId);
    List<OrderDTO> getUserOrders(Long userId, int page, int pageSize);

}
