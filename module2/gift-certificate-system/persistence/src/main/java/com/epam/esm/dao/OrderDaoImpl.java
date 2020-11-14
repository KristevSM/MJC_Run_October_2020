package com.epam.esm.dao;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.model.Order;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {
    @Override
    public Optional<Order> find(Long id) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        Optional<Order> order;
        try {
            String hql = "FROM Order o WHERE o.id = :id";
            Query query = session.createQuery(hql);
            query.setParameter("id", id);
            order = query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a order: {0}", e.getMessage()));
        } finally {
            session.close();
        }
        return order;
    }

    @Override
    public Long save(Order order) {

        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.saveOrUpdate(order);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw new DaoException(MessageFormat.format("Unable make order: {0}", e.getMessage()));
        } finally {
            session.close();
        }
        return order.getId();
    }

    @Override
    public void update(Order order) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.update(order);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw new DaoException(MessageFormat.format("Unable update order: {0}", e.getMessage()));
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Long id) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.delete(session.get(Order.class, id));
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable delete a order: {0}", e.getMessage()));
        } finally {
            session.close();
        }
    }

    @Override
    public List<Order> findAll(int from, int pageSize) {

        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        List<Order> firstPage = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(Order.class);
            criteria.setFirstResult(from);
            criteria.setMaxResults(pageSize);
            firstPage = criteria.list();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of orders: {0}", e.getMessage()));
        } finally {
            session.close();
        }
        return firstPage;
    }

    @Override
    public Optional<OrderDto> getOrderDetails(Long userId, Long orderId) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        OrderDto orderDto;
        try {
            String hql = "FROM Order o WHERE o.id = :orderId AND o.user.id = :userId";
            Query query = session.createQuery(hql);
            query.setParameter("orderId", orderId);
            query.setParameter("userId", userId);
            Optional<Order> order = query.uniqueResultOptional();
            if (!order.isPresent()) {
                return Optional.empty();
            }
            Order orderFromDto = order.get();
            orderDto = OrderDto.builder()
                    .id(orderFromDto.getId())
                    .userName(orderFromDto.getUser().getFirstName() + " " + orderFromDto.getUser().getLastName())
                    .certificateName(orderFromDto.getGiftCertificate().getName())
                    .purchaseCost(orderFromDto.getGiftCertificate().getPrice())
                    .purchaseTime(orderFromDto.getOrderDate())
                    .build();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a order: {0}", e.getMessage()));
        } finally {
            session.close();
        }
        return Optional.of(orderDto);    }

    @Override
    public List<OrderDto> getUserOrders(Long userId, int from, int pageSize) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        List<Order> orders;
        List<OrderDto> firstPageDto = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(Order.class);
            criteria.add(Restrictions.eq("user.id", userId));
            criteria.setFirstResult(from);
            criteria.setMaxResults(pageSize);
            orders = criteria.list();
            for (Order order : orders) {
                OrderDto orderDto = OrderDto.builder()
                        .id(order.getId())
                        .userName(order.getUser().getFirstName() + " " + order.getUser().getLastName())
                        .purchaseTime(order.getOrderDate())
                        .build();
                if (order.getGiftCertificate() != null) {
                    orderDto.setCertificateName(order.getGiftCertificate().getName());
                    orderDto.setPurchaseCost(order.getGiftCertificate().getPrice());
                }
                firstPageDto.add(orderDto);
            }

        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of orders: {0}", e.getMessage()));
        } finally {
            session.close();
        }
        return firstPageDto;    }
}
