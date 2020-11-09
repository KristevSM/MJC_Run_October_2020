package com.epam.esm.dao;

import com.epam.esm.model.User;
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
    public List<User> getAllUsers() {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        String sql = "SELECT * FROM USERS";
        Query query = session.createNativeQuery(sql).addEntity(User.class);
        List<User> userList = query.list();
        session.close();
        return userList;
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
