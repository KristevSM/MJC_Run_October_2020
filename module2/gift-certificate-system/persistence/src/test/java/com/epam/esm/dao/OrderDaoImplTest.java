package com.epam.esm.dao;

import com.epam.esm.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class OrderDaoImplTest {

    private OrderDao orderDao;
    @BeforeEach
    void setUp() {
        orderDao = new OrderDaoImpl();
    }

    @Test
    public void shouldFindOrderById() {
        Optional<Order> order1 = orderDao.find(1L);
        Optional<Order> order2 = orderDao.find(2L);
        Optional<Order> order3 = orderDao.find(3L);
        Optional<Order> orderNotExists = orderDao.find(150L);
        assertTrue(order1.isPresent());
        assertTrue(order2.isPresent());
        assertTrue(order3.isPresent());
        assertFalse(orderNotExists.isPresent());
    }

    @Test
    public void shouldGetOrdersList() {
        List<Order> orderList = orderDao.findAll(1L, 200L);
        assertEquals(6L, orderList.size());
    }

    @Test
    public void shouldPaginateOrdersList() {
        List<Order> orderList1 = orderDao.findAll(1L, 5L);
        assertEquals(5L, orderList1.size());
        List<Order> orderList2 = orderDao.findAll(2L, 5L);
        assertEquals(1L, orderList2.size());
    }
}