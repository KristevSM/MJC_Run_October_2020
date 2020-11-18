package com.epam.esm.dao;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends CrudDAO<Order>{
    Optional<OrderDto> getOrderDetails(Long userId,Long orderId);
    List<OrderDto> getUserOrders(Long userId, Long page, Long pageSize);
    Long findOrderTotalCountByUserId(Long userId);
}
