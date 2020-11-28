package com.epam.esm.dao;

import com.epam.esm.model.Order;

import java.util.List;

public interface OrderDao extends CrudDAO<Order>{
    List<Order> getUserOrders(Long userId, Long page, Long pageSize);
    Long findOrderTotalCountByUserId(Long userId);
    Long findOrderTotalCount();
}
