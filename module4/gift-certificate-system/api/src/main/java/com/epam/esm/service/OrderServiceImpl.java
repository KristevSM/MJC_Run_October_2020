package com.epam.esm.service;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.ZonedDateTime;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderConverter orderConverter, UserRepository userRepository, GiftCertificateRepository giftCertificateRepository) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.userRepository = userRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    public Page<OrderDTO> getAllOrders(int page, int pageSize) {
        try {
            return orderRepository.findAll(PageRequest.of(page, pageSize))
                    .map(orderConverter::convertFromEntity);
        } catch (Exception e) {
            log.error("IN getAllOrders - Unable to find the list of orders: {}", e.getMessage());
            throw new DaoException("Unable to find the list of orders");
        }
    }

    @Override
    public OrderDTO getOrderById(Long id) {
            Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                    .format("Order with id: {0} not found", id)));
            return orderConverter.convertFromEntity(order);
    }

    @Override
    public OrderDTO makeOrder(Long userId, Long certificateId) {
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
        try {
            Order newOrder= orderRepository.save(order);
            Order orderFromDao = orderRepository.findById(newOrder.getId()).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                    .format("Order with id: {0} not found", newOrder.getId())));
            return orderConverter.convertFromEntity(orderFromDao);
        } catch (Exception e) {
            log.error("IN makeOrder - Unable to make a new order: {}", e.getMessage());
            throw new DaoException("Unable to make a new order");
        }
    }

    @Override
    public void removeOrder(Long orderId) {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                    .format("Order with id: {0} not found", orderId)));
            orderRepository.delete(order);
    }

    @Override
    public Page<OrderDTO> getUserOrders(Long userId, int page, int pageSize) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(MessageFormat
                .format("User with id: {0} not found", userId)));
        return orderRepository.findByUserId(userId, PageRequest.of(page, pageSize))
                .map(orderConverter::convertFromEntity);
    }

}
