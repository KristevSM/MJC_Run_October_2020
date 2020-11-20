package com.epam.esm.dao;

import com.epam.esm.exception.DaoException;
import com.epam.esm.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
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
public class UserDaoImpl implements UserDao {

    private static final String FIND_USER_BY_ID = "FROM User u WHERE u.id = :id";

    @PersistenceContext
    EntityManager entityManager;

    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public Optional<User> find(Long id) {
        Session session = getCurrentSession();
        Optional<User> user;
        try {
            Query query = session.createQuery(FIND_USER_BY_ID);
            query.setParameter("id", id);
            user = query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a user: {0}", e.getMessage()));
        }
        return user;
    }

    @Override
    public Long save(User model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(User model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findAll(Long page, Long pageSize) {
        Session session = getCurrentSession();
        List<User> firstPage = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(User.class);

            criteria.setFirstResult(Math.toIntExact((page - 1) * pageSize));
            criteria.setMaxResults(Math.toIntExact(pageSize));
            firstPage = criteria.list();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of users: {0}", e.getMessage()));
        }
        return firstPage;
    }

    @Override
    public Long findUsersTotalCount() {
        Session session = getCurrentSession();
        Long totalCount = 0L;
        try {
            Criteria criteria = session.createCriteria(User.class);
            totalCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();

        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of users: {0}", e.getMessage()));
        }
        return totalCount;
    }
}
