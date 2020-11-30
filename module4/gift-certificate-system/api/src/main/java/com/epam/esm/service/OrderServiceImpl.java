package com.epam.esm.service;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.OrderDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
    public List<OrderDTO> getAllOrders(int page, int pageSize) {
        Page<Order> orderPage = orderRepository.findAll(PageRequest.of(page, pageSize));
        List<Order> orderList = orderPage.toList();
        return orderList.stream()
                .map(orderConverter::convertOrderDTOFromOrder)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                .format("Order with id: {0} not found", id)));
        return orderConverter.convertOrderDTOFromOrder(order);
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
        Order newOrder= orderRepository.save(order);
        Order orderFromDao = orderRepository.findById(newOrder.getId()).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                .format("Order with id: {0} not found", newOrder.getId())));
        return orderConverter.convertOrderDTOFromOrder(orderFromDao);
    }

    @Override
    public void removeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                .format("Order with id: {0} not found", orderId)));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDTO> getUserOrders(Long userId, int page, int pageSize) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(MessageFormat
                .format("User with id: {0} not found", userId)));
        List<Order> orderList = orderRepository.findByUserId(userId, PageRequest.of(page, pageSize));
        return orderList.stream()
                .map(orderConverter::convertOrderDTOFromOrder)
                .collect(Collectors.toList());}

}
