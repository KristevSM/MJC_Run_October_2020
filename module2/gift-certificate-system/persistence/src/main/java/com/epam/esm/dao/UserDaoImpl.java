package com.epam.esm.dao;

import com.epam.esm.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao{

    private SessionFactory sessionFactory;

    @Override
    public List<User> getAllUsers(int from, int pageSize) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(User.class);
        criteria.setFirstResult(from);
        criteria.setMaxResults(pageSize);
        List<User> firstPage = criteria.list();

        session.close();
        return firstPage;
    }

    @Override
    public Optional<User> find(Long id) {
        return Optional.empty();
    }

    @Override
    public Long save(User model) {
        return null;
    }

    @Override
    public void update(User model) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
