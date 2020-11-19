package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final UserDao userDao;
    private final GiftCertificateDao certificateDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserDao userDao, GiftCertificateDao certificateDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.certificateDao = certificateDao;
    }

    @Override
    public List<Order> getAllOrders(Long page, Long pageSize) {
        return orderDao.findAll(page, pageSize);
    }

    @Override
    @Transactional
    public Order makeOrder(Long userId, Long certificateId) {
        GiftCertificate certificate = certificateDao.find(certificateId).orElseThrow(() -> new GiftCertificateNotFoundException(MessageFormat
                .format("Gift certificate with id: {0} not found", certificateId)));

        User user = userDao.find(userId).orElseThrow(() -> new UserNotFoundException(MessageFormat
                .format("User with id: {0} not found", userId)));
        Order order = Order.builder()
                .giftCertificate(certificate)
                .cost(certificate.getPrice())
                .user(user)
                .orderDate(ZonedDateTime.now())
                .build();
        Long orderId = orderDao.save(order);
        Order orderFromDao = orderDao.find(orderId).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                .format("Order with id: {0} not found", orderId)));
        return orderFromDao;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderDao.find(id).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                .format("Order with id: {0} not found", id)));
    }

    @Override
    public void removeOrder(Long orderId) {
        orderDao.find(orderId).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                .format("Order with id: {0} not found", orderId)));
        orderDao.delete(orderId);
    }


    @Override
    public List<Order> getUserOrders(Long userId, Long page, Long pageSize) {
        userDao.find(userId).orElseThrow(() -> new UserNotFoundException(MessageFormat
                .format("User with id: {0} not found", userId)));
        return orderDao.getUserOrders(userId, page, pageSize);
    }

    @Override
    public Long findOrderTotalCountByUserId(Long userId) {
        return orderDao.findOrderTotalCountByUserId(userId);
    }

    @Override
    public Long findOrderTotalCount() {
        return orderDao.findOrderTotalCount();
    }
}
