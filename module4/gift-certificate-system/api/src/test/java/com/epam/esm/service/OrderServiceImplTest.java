package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;


    @Test
    void shouldFindOrderById() {
        OrderDTO order1 = orderService.getOrderById(1L);
        OrderDTO order2 = orderService.getOrderById(2L);
        assertEquals(1L, order1.getId());
        assertEquals(2L, order2.getId());
    }

    @Test
    void shouldMakeOrder() {
        OrderDTO order = orderService.makeOrder(1L, 2L);
        assertEquals(6L, order.getId());
    }
}