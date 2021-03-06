package com.epam.esm.dao;

import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.model.Tag;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Transactional
@Repository
@Primary
public class TagDaoImpl implements TagDao {

    private static final String FIND_TAG_BY_NAME = "FROM Tag t WHERE t.name = :name";
    private static final String FIND_TAG_BY_ID = "FROM Tag t WHERE t.id = :id";
    private static final String DELETE_TAG_BY_ID = "DELETE Tag t WHERE t.id = :id";

    private static final String GET_USER_MOST_WIDELY_USED_TAG = "SELECT tag.tag_id, name, COUNT(name) AS qty from orders\n" +
            "inner join users u on u.user_id = orders.user_id\n" +
            "inner join tag_has_gift_certificate on (orders.certificate_id=tag_has_gift_certificate.gift_certificate_id)\n" +
            "inner join tag on (tag.tag_id=tag_has_gift_certificate.tag_id) where orders.user_id = \n" +
            "(select orders.user_id from orders\n" +
            "group by orders.user_id\n" +
            "order by sum(orders.cost) desc\n" +
            "limit 1) \n" +
            "group by tag.name\n" +
            "order by qty desc\n" +
            "limit 1";


    @PersistenceContext
    EntityManager entityManager;

    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public Optional<Tag> findByTagName(String tagName) {
        Session session = getCurrentSession();
        Optional<Tag> tag;
        try {
            Query query = session.createQuery(FIND_TAG_BY_NAME);
            query.setParameter("name", tagName);
            tag = query.uniqueResultOptional();
        } catch (Exception e) {
            throw new OrderNotFoundException(MessageFormat.format("Unable to get a tag: {0}", e.getMessage()));
        }
        return tag;
    }

    @Override
    public Optional<Tag> find(Long id) {
        Session session = getCurrentSession();
        Optional<Tag> tag;
        try {
            Query query = session.createQuery(FIND_TAG_BY_ID);
            query.setParameter("id", id);
            tag = query.uniqueResultOptional();
        } catch (Exception e) {
            throw new OrderNotFoundException(MessageFormat.format("Unable to get a tag: {0}", e.getMessage()));
        }
        return tag;
    }

    @Override
    public Long save(Tag tag) {
        Session session = getCurrentSession();
        try {
            session.save(tag);
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable save tag: {0}", e.getMessage()));
        }
        return tag.getId();
    }

    @Override
    public void update(Tag tag) {
        Session session = getCurrentSession();
        try {
            session.clear();
            session.update(tag);
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable update tag: {0}", e.getMessage()));
        }
    }

    @Override
    public void delete(Long id) {
        Session session = getCurrentSession();
        try {
            session.clear();
            Query query = session.createQuery(DELETE_TAG_BY_ID);
            query.setParameter("id", id);
            query.executeUpdate();
            session.flush();
            ;
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable delete a tag: {0}", e.getMessage()));
        }
    }

    @Override
    public List<Tag> findAll(Long page, Long pageSize) {
        Session session = getCurrentSession();
        List<Tag> firstPage = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(Tag.class);
            criteria.setFirstResult(Math.toIntExact((page - 1) * pageSize));
            criteria.setMaxResults(Math.toIntExact(pageSize));
            firstPage = criteria.list();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of tags: {0}", e.getMessage()));
        }
        return firstPage;
    }

    @Override
    public Optional<Tag> getUsersMostWidelyUsedTag() {
        Session session = getCurrentSession();
        Optional tag;
        try {
            Query query = session.createSQLQuery(GET_USER_MOST_WIDELY_USED_TAG).addEntity(Tag.class);
            tag = query.uniqueResultOptional();

        } catch (Exception e) {
            throw new OrderNotFoundException(MessageFormat.format("Unable to get a tag: {0}", e.getMessage()));
        }
        return tag;
    }
}
