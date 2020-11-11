package com.epam.esm.dao;

import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao{
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
            throw new OrderNotFoundException(MessageFormat.format("Unable to get a order: {0}", e.getMessage()));
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
            if (session.getTransaction()!=null)  {
                session.getTransaction().rollback();
            }
            throw new DaoException(MessageFormat.format("Unable make order: {0}", e.getMessage()));
        } finally {
            session.close();
        }
        return order.getId();
    }

    @Override
    public void update(Order model) {

    }

    @Override
    public void delete(Long id) {

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
}
