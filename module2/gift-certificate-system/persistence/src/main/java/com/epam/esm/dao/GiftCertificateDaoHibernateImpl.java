package com.epam.esm.dao;

import com.epam.esm.dao.jdbc.CertificateSearchQuery;
import com.epam.esm.exception.DaoException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.User;
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
public class GiftCertificateDaoHibernateImpl implements GiftCertificateDao {
    @Override
    public List<GiftCertificate> getCertificates(CertificateSearchQuery query) {
        return null;
    }

    @Override
    public Optional<GiftCertificate> find(Long id) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        Optional<GiftCertificate> giftCertificate;
        try {
            String hql = "FROM GiftCertificate c WHERE c.id = :id";
            Query query = session.createQuery(hql);
            query.setParameter("id", id);
            giftCertificate = query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a certificate: {0}", e.getMessage()));
        } finally {
            session.close();
        }
        return giftCertificate;
    }

    @Override
    public Long save(GiftCertificate certificate) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.persist(certificate);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw new DaoException(MessageFormat.format("Unable save certificate: {0}", e.getMessage()));
        } finally {
            session.close();
        }
        return certificate.getId();
    }

    @Override
    public void update(GiftCertificate certificate) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.update(certificate);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw new DaoException(MessageFormat.format("Unable update certificate: {0}", e.getMessage()));
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Long id) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            String hql = "DELETE GiftCertificate c WHERE c.id = :id";
            Query query = session.createQuery(hql);
            query.setParameter("id", id);
            query.executeUpdate();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw new DaoException(MessageFormat.format("Unable delete certificate: {0}", e.getMessage()));
        } finally {
            session.close();
        }
    }

    @Override
    public List<GiftCertificate> findAll(int from, int pageSize) {
        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
        List<GiftCertificate> firstPage = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(User.class);
            criteria.setFirstResult(from);
            criteria.setMaxResults(pageSize);
            firstPage = criteria.list();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of certificates: {0}", e.getMessage()));
        } finally {
            session.close();
        }
        return firstPage;
    }
}
