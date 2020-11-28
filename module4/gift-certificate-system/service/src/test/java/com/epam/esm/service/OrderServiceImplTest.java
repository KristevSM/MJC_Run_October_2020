package com.epam.esm.service;

import com.epam.esm.dao.*;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {

    private OrderService orderService;
    private OrderDao orderDao;
    private UserDao userDao;
    private GiftCertificateDao certificateDao;

    @BeforeEach
    void setUp() {
        this.orderDao = mock(OrderDaoImpl.class);
        this.userDao = mock(UserDaoImpl.class);
        this.certificateDao = mock(GiftCertificateDaoImpl.class);
        this.orderService = new OrderServiceImpl(orderDao, userDao, certificateDao);
    }

    @Test
    void shouldReturnAllOrders() {

        List<Order> orders = mock(ArrayList.class);
        when(orders.size()).thenReturn(10);
        when(orderService.getAllOrders(1L, 20L)).thenReturn(orders);

        assertEquals(10, orderService.getAllOrders(1L, 20L).size());
        Mockito.verify(orderDao, Mockito.times(1)).findAll(1L, 20L);
    }

    @Test
    void shouldFindOrderById() {

        Order order = mock(Order.class);
        when(orderDao.find(1L)).thenReturn(Optional.of(order));

        assertEquals(order, orderService.getOrderById(1L));
        Mockito.verify(orderDao, Mockito.times(1)).find(1L);
    }

    @Test
    void shouldThrowExceptionWhenOrderByIdNotFound() {

        Order order = mock(Order.class);
        when(orderDao.find(2L)).thenReturn(Optional.of(order));

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById(1L);
        });
        Mockito.verify(orderDao, Mockito.times(1)).find(1L);
    }

    @Test
    void shouldRemoveOrder() {
        Order order = mock(Order.class);
        when(orderDao.find(1L)).thenReturn(Optional.ofNullable(order));
        orderService.removeOrder(1L);
        Mockito.verify(orderDao, Mockito.times(1)).delete(1L);
    }

    @Test
    void shouldReturnUsersOrders() {

        User user = mock(User.class);
        List<Order> orders = mock(ArrayList.class);
        when(orders.size()).thenReturn(10);
        when(userDao.find(1L)).thenReturn(Optional.ofNullable(user));
        when(orderService.getUserOrders(1L,1L, 20L)).thenReturn(orders);

        assertEquals(10, orderService.getUserOrders(1L,1L, 20L).size());
        Mockito.verify(orderDao, Mockito.times(1)).getUserOrders(1L,1L, 20L);
    }

    @Test
    void shouldMakeOrder() {
        User user = mock(User.class);
        GiftCertificate certificate = mock(GiftCertificate.class);
        when(userDao.find(1L)).thenReturn(Optional.ofNullable(user));
        when(certificateDao.find(1L)).thenReturn(Optional.ofNullable(certificate));
        Order order = Order.builder()
                .giftCertificate(certificate)
                .cost(certificate.getPrice())
                .user(user)
                .orderDate(ZonedDateTime.now())
                .build();
        when(orderDao.save(order)).thenReturn(1L);
        orderDao.save(order);
        Mockito.verify(orderDao, Mockito.times(1)).save(order);
    }
}