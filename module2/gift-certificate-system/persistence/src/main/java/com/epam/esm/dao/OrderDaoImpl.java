package com.epam.esm.dao;

import com.epam.esm.exception.DaoException;
import com.epam.esm.model.Order;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class OrderDaoImpl implements OrderDao {

    private static final String FIND_ORDER_BY_ID = "FROM Order o WHERE o.id = :id";

    @PersistenceContext
    EntityManager entityManager;

    protected Session getCurrentSession()  {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public Optional<Order> find(Long id) {
        Session session = getCurrentSession();
        Optional<Order> order;
        try {
            Query query = session.createQuery(FIND_ORDER_BY_ID);
            query.setParameter("id", id);
            order = query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a order: {0}", e.getMessage()));
        }
        return order;
    }

    @Override
    public Long save(Order order) {

        Session session = getCurrentSession();
        try {
            session.saveOrUpdate(order);
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable make order: {0}", e.getMessage()));
        }
        return order.getId();
    }

    @Override
    public void update(Order order) {
        Session session = getCurrentSession();
        try {
            session.update(order);
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable update order: {0}", e.getMessage()));
        }
    }

    @Override
    public void delete(Long id) {
        Session session = getCurrentSession();
        try {
            session.delete(session.get(Order.class, id));
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable delete a order: {0}", e.getMessage()));
        }
    }

    @Override
    public List<Order> findAll(Long page, Long pageSize) {

        Session session = getCurrentSession();
        List<Order> firstPage = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(Order.class);
            criteria.setFirstResult(Math.toIntExact((page - 1) * pageSize));
            criteria.setMaxResults(Math.toIntExact(pageSize));
            firstPage = criteria.list();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of orders: {0}", e.getMessage()));
        }
        return firstPage;
    }

    @Override
    public List<Order> getUserOrders(Long userId, Long page, Long pageSize) {
        Session session = getCurrentSession();
        List<Order> orders = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(Order.class);
            criteria.add(Restrictions.eq("user.id", userId));

            criteria.setFirstResult(Math.toIntExact((page - 1) * pageSize));
            criteria.setMaxResults(Math.toIntExact(pageSize));
            orders = criteria.list();
//            Long totalCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();

        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of orders: {0}", e.getMessage()));
        }
        return orders;
    }

    @Override
    public Long findOrderTotalCountByUserId(Long userId) {
        Session session = getCurrentSession();
        Long totalCount = 0L;
        try {
            Criteria criteria = session.createCriteria(Order.class);
            criteria.add(Restrictions.eq("user.id", userId));

            totalCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();

        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of orders: {0}", e.getMessage()));
        }
        return totalCount;
    }

    @Override
    public Long findOrderTotalCount() {
        Session session = getCurrentSession();
        Long totalCount = 0L;
        try {
            Criteria criteria = session.createCriteria(Order.class);
            totalCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();

        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of orders: {0}", e.getMessage()));
        }
        return totalCount;
    }
}
