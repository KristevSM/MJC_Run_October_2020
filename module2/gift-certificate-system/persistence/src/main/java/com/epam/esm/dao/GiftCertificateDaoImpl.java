package com.epam.esm.dao;

import com.epam.esm.exception.DaoException;
import com.epam.esm.model.GiftCertificate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Transactional
@Repository
@Primary
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private static final String FIND_CERTIFICATES_BY_TAGS_NAMES = "SELECT c FROM GiftCertificate c LEFT JOIN c.tags t WHERE t.name IN :tagNames"
            + " GROUP BY c HAVING COUNT(t.name) = :tagNamesSize";
    private static final String FIND_CERTIFICATE_BY_ID = "FROM GiftCertificate c WHERE c.id = :id";
    private static final String DELETE_CERTIFICATE_BY_ID = "DELETE GiftCertificate c WHERE c.id = :id";
    private static final String FIND_CERTIFICATE_BY_NAME = "FROM GiftCertificate c WHERE c.name = :name";

    @PersistenceContext
    EntityManager entityManager;

    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public Optional<GiftCertificate> find(Long id) {
        Session session = getCurrentSession();
        Optional<GiftCertificate> giftCertificate;
        try {
            Query query = session.createQuery(FIND_CERTIFICATE_BY_ID);
            query.setParameter("id", id);
            giftCertificate = query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a certificate: {0}", e.getMessage()));
        }
        return giftCertificate;
    }

    @Override
    public Long save(GiftCertificate certificate) {
        Session session = getCurrentSession();
        try {
            session.clear();
            session.persist(certificate);
            session.flush();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable save certificate: {0}", e.getMessage()));
        }
        return certificate.getId();
    }

    @Override
    public void update(GiftCertificate certificate) {
        Session session = getCurrentSession();
        try {
            session.clear();
            session.update(certificate);
            session.flush();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable update certificate: {0}", e.getMessage()));
        }
    }

    @Override
    public void delete(Long id) {
        Session session = getCurrentSession();
        try {
            session.clear();
            Query query = session.createQuery(DELETE_CERTIFICATE_BY_ID);
            query.setParameter("id", id);
            query.executeUpdate();
            session.flush();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable delete certificate: {0}", e.getMessage()));
        }
    }

    @Override
    public List<GiftCertificate> findAll(Long page, Long pageSize) {
        Session session = getCurrentSession();
        List<GiftCertificate> firstPage = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(GiftCertificate.class);
            criteria.setFirstResult(Math.toIntExact((page - 1) * pageSize));
            criteria.setMaxResults(Math.toIntExact(pageSize));
            firstPage = criteria.list();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of certificates: {0}", e.getMessage()));
        }
        return firstPage;
    }

    @Override
    public List<GiftCertificate> getCertificates(CertificateSearchQuery query, Long page, Long pageSize) {

        Session session = getCurrentSession();
        List<GiftCertificate> firstPage = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(GiftCertificate.class);
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> certificateRoot = criteriaQuery.from(GiftCertificate.class);

            if (query.hasTagName()) {
                criteria.createAlias("tags", "tag");
                criteria.add(Restrictions.eq("tag.name", query.getTagName()));
                criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

            }
            if (query.hasPartOfDescription()) {
                criteria.add(Restrictions.like("description", "%" + query.getPartOfDescription() + "%"));

            }
            if (query.hasPartOfName()) {
                criteria.add(Restrictions.like("name", "%" + query.getPartOfName() + "%"));
            }
            if (query.hasSortParameter()) {
                if ("DESC".equals(query.getSortOrder())) {
                    criteria.addOrder(org.hibernate.criterion.Order.desc(query.getSortParameter()));
                } else {
                    criteria.addOrder(org.hibernate.criterion.Order.asc(query.getSortParameter()));
                }
            }
            criteria.setFirstResult(Math.toIntExact((page - 1) * pageSize));
            criteria.setMaxResults(Math.toIntExact(pageSize));
            firstPage = criteria.list();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of certificates: {0}", e.getMessage()));
        }
        return firstPage;
    }

    @Override

    public List<GiftCertificate> findCertificatesByTags(List<String> tagNames, Long page, Long pageSize) {
        Session session = getCurrentSession();
        List<GiftCertificate> firstPage = new ArrayList<>();
        try {
            Query query = session.createQuery(FIND_CERTIFICATES_BY_TAGS_NAMES);
            Long tagsSize = Long.valueOf(tagNames.size());
            query.setParameter("tagNames", tagNames);
            query.setParameter("tagNamesSize", tagsSize);
            query.setFirstResult(Math.toIntExact(Math.toIntExact((page - 1) * pageSize)));
            query.setMaxResults(Math.toIntExact(Math.toIntExact(pageSize)));
            firstPage = query.list();

        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a list of certificates: {0}", e.getMessage()));
        }
        return firstPage;
    }

    @Override
    public Optional<GiftCertificate> getCertificateByName(String name) {
        Session session = getCurrentSession();
        Optional<GiftCertificate> giftCertificate;
        try {
            Query query = session.createQuery(FIND_CERTIFICATE_BY_NAME);
            query.setParameter("name", name);
            giftCertificate = query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DaoException(MessageFormat.format("Unable to get a certificate: {0}", e.getMessage()));
        }
        return giftCertificate;
    }
}
