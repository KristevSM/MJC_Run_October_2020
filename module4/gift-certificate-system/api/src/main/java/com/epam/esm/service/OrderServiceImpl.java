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

/**
 * @author Sergei Kristev
 * <p>
 * Service for managing Orders objects.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final UserDao userDao;
    private final GiftCertificateDao certificateDao;

    /**
     * Constructor accepts UserDao object.
     *
     * @param orderDao       OrderDao instance.
     * @param userDao        UserDao instance.
     * @param certificateDao GiftCertificateDao instance.
     */
    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserDao userDao, GiftCertificateDao certificateDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.certificateDao = certificateDao;
    }

    /**
     * Gets list of all orders from OrderDao.
     *
     * @param page     Index of first page instance.
     * @param pageSize Count pages in response.
     * @return Orders list.
     */
    @Override
    public List<Order> getAllOrders(Long page, Long pageSize) {
        return orderDao.findAll(page, pageSize);
    }

    /**
     * Create order on certificate.
     *
     * @param userId        User's id.
     * @param certificateId Certificate's id.
     * @return Order instance.
     */
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

    /**
     * Gets order by id.
     *
     * @param id Order id.
     * @return Order instance.
     */
    @Override
    public Order getOrderById(Long id) {
        return orderDao.find(id).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                .format("Order with id: {0} not found", id)));
    }

    /**
     * Remove order.
     *
     * @param orderId   Order's id.
     *
     */
    @Override
    public void removeOrder(Long orderId) {
        orderDao.find(orderId).orElseThrow(() -> new OrderNotFoundException(MessageFormat
                .format("Order with id: {0} not found", orderId)));
        orderDao.delete(orderId);
    }


    /**
     * Gets user's orders.
     *
     * @param userId   User's id.
     * @param page     Index of first page instance.
     * @param pageSize Count pages in response.
     * @return List orders.
     */
    @Override
    public List<Order> getUserOrders(Long userId, Long page, Long pageSize) {
        userDao.find(userId).orElseThrow(() -> new UserNotFoundException(MessageFormat
                .format("User with id: {0} not found", userId)));
        return orderDao.getUserOrders(userId, page, pageSize);
    }

    /**
     * Gets total count of user's orders.
     *
     * @return Long orders count.
     */
    @Override
    public Long findOrderTotalCountByUserId(Long userId) {
        return orderDao.findOrderTotalCountByUserId(userId);
    }

    /**
     * Gets total count of orders in DB.
     *
     * @return Long orders count.
     */
    @Override
    public Long findOrderTotalCount() {
        return orderDao.findOrderTotalCount();
    }
}
