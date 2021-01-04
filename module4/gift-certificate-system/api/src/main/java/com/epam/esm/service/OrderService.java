package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.model.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    Page<OrderDTO> getAllOrders(int page, int pageSize);
    OrderDTO getOrderById(Long id);
    OrderDTO makeOrder(Long userId, Long certificateId);
    void removeOrder(Long orderId);
    Page<OrderDTO> getUserOrders(Long userId, int page, int pageSize);

}
