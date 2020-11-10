package com.epam.esm.dao;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao{
    @Override
    public Optional<Order> find(Long id) {
        return Optional.empty();
    }

    @Override
    public Long save(Order order) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        return (Long) session.save(order);
    }

    @Override
    public void update(Order model) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Order> findAll() {
        return null;
    }
}
