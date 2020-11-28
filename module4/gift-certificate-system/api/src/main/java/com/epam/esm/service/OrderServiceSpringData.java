package com.epam.esm.service;

import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Primary
public class OrderServiceSpringData implements OrderService{

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    @Autowired
    public OrderServiceSpringData(OrderRepository orderRepository, UserRepository userRepository, GiftCertificateRepository giftCertificateRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    public List<Order> getAllOrders(Long page, Long pageSize) {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                .format("Order with id: {0} not found", id)));    }

    @Override
    public Order makeOrder(Long userId, Long certificateId) {
        GiftCertificate certificate = giftCertificateRepository.findById(certificateId).orElseThrow(() -> new GiftCertificateNotFoundException(MessageFormat
                .format("Gift certificate with id: {0} not found", certificateId)));

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(MessageFormat
                .format("User with id: {0} not found", userId)));
        Order order = Order.builder()
                .giftCertificate(certificate)
                .cost(certificate.getPrice())
                .user(user)
                .orderDate(ZonedDateTime.now())
                .build();
        Order newOrder= orderRepository.save(order);
        Order orderFromDao = orderRepository.findById(newOrder.getId()).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                .format("Order with id: {0} not found", newOrder.getId())));
        return orderFromDao;    }

    @Override
    public void removeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                .format("Order with id: {0} not found", orderId)));
        orderRepository.delete(order);
    }

    @Override
    public List<Order> getUserOrders(Long userId, Long page, Long pageSize) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(MessageFormat
                .format("User with id: {0} not found", userId)));
        return orderRepository.findByUserId(userId);    }

    @Override
    public Long findOrderTotalCountByUserId(Long userId) {
        return null;
    }

    @Override
    public Long findOrderTotalCount() {
        return null;
    }
}
