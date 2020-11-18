package com.epam.esm.dao;

import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.model.Tag;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class TagDaoImpl implements TagDao {
    @Override
    public Optional<Tag> findByTagName(String tagName) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        Optional<Tag> tag;
        try {
            String hql = "FROM Tag t WHERE t.name = :name";
            Query query = session.createQuery(hql);
            query.setParameter("name", tagName);
            tag = query.uniqueResultOptional();
        } catch (Exception e) {
            throw new OrderNotFoundException(MessageFormat.format("Unable to get a tag: {0}", e.getMessage()));
        } finally {
            session.close();
        }
        return tag;
    }

    @Override
    public Optional<Tag> find(Long id) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        Optional<Tag> tag;
        try {
            String hql = "FROM Tag t WHERE t.id = :id";
            Query query = session.createQuery(hql);
            query.setParameter("id", id);
            tag = query.uniqueResultOptional();
        } catch (Exception e) {
            throw new OrderNotFoundException(MessageFormat.format("Unable to get a tag: {0}", e.getMessage()));
        } finally {
            session.close();
        }
        return tag;
    }

    @Override
    public Long save(Tag tag) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.save(tag);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw new DaoException(MessageFormat.format("Unable save tag: {0}", e.getMessage()));
        } finally {
            session.close();
        }
        return tag.getId();    }

    @Override
    public void update(Tag tag) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.update(tag);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw new DaoException(MessageFormat.format("Unable update tag: {0}", e.getMessage()));
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Long id) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.delete(session.get(Tag.class, id));
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable delete a tag: {0}", e.getMessage()));
        } finally {
            session.close();
        }
    }

    @Override
    public List<Tag> findAll(Long page, Long pageSize) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        List<Tag> firstPage = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(Tag.class);
            criteria.setFirstResult(Math.toIntExact((page - 1) * pageSize));
            criteria.setMaxResults(Math.toIntExact(pageSize));
            firstPage = criteria.list();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of tags: {0}", e.getMessage()));
        } finally {
            session.close();
        }
        return firstPage;    }

    @Override
    public Optional<Tag> getUsersMostWidelyUsedTag() {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Optional tag;
        try {
            String sql = "SELECT tag.tag_id, tag.name, sum(orders.cost) as totalSum from orders\n" +
                    "inner join users u on u.user_id = orders.user_id\n" +
                    "inner join tag_has_gift_certificate on (orders.certificate_id=tag_has_gift_certificate.gift_certificate_id)\n" +
                    "inner join tag on (tag.tag_id=tag_has_gift_certificate.tag_id)  \n" +
                    "group by tag.name\n" +
                    "ORDER BY totalSum DESC\n" +
                    "LIMIT 1";
            Query query = session.createSQLQuery(sql).addEntity(Tag.class);
            tag = query.uniqueResultOptional();

        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new OrderNotFoundException(MessageFormat.format("Unable to get a tag: {0}", e.getMessage()));
        } finally {
            session.getTransaction().commit();
            session.close();
        }
        return tag;
    }
}
