package com.epam.esm.dao;

import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GiftCertificateDaoImplTest {

    private GiftCertificateDao giftCertificateDao;
    private TagDaoImpl tagDao;

    @BeforeEach
    void setUp() {
        giftCertificateDao = new GiftCertificateDaoImpl();
        tagDao = new TagDaoImpl();
    }
    //
//    //Hibernate
//    @Test
//    public void shouldSaveCertificateWithTags() {
//        Session session = HibernateAnnotationUtil.getSessionFactory().openSession();
//        session.beginTransaction();
//
//        GiftCertificate certificate = GiftCertificate.builder()
//                .name("New certificate")
//                .description("Some description")
//                .price(BigDecimal.valueOf(100D))
//                .createDate(ZonedDateTime.now())
//                .lastUpdateDate(ZonedDateTime.now())
//                .duration(6)
//                .tags(new ArrayList<>())
//                .build();
//
//        Tag tag = Tag.builder()
//                .name("Tag 4")
//                .build();
//
//        certificate.getTags().add(tag);
//
//        session.persist(certificate);
//
//        session.getTransaction().commit();
//        session.close();
//
//        session = HibernateAnnotationUtil.getSessionFactory().openSession();
//        String sql = "SELECT * FROM GIFT_CERTIFICATE";
//        Query query = session.createNativeQuery(sql).addEntity(GiftCertificate.class);
//        List<GiftCertificate> certificates = query.list();
//
//        String sql2 = "SELECT * FROM TAG";
//        Query query2 = session.createNativeQuery(sql2).addEntity(Tag.class);
//        List<Tag> tags = query2.list();
//
//        session.close();
//    }
//
    @Test
    void shouldFindAllCertificates() {
        List<GiftCertificate> giftCertificates = giftCertificateDao.findAll(1L, 16L);
        assertEquals(15, giftCertificates.size());
    }

    @Test
    void shouldFindCertificateById() {
        Optional<GiftCertificate> giftCertificate1 = giftCertificateDao.find(1L);
        Optional<GiftCertificate> giftCertificate15 = giftCertificateDao.find(15L);
        assertTrue(giftCertificate1.isPresent());
        assertTrue(giftCertificate15.isPresent());

    }

    @Test
    void shouldUpdateCertificate() {

        Optional<GiftCertificate> certificateFromDao = giftCertificateDao.find(1L);
        GiftCertificate certificate = certificateFromDao.get();

        assertNotEquals("Updated certificate", certificate.getName());
        assertNotEquals("Updated description", certificate.getDescription());

        certificate.setName("Updated certificate");
        certificate.setDescription("Updated description");

        giftCertificateDao.update(certificate);

        Optional<GiftCertificate> certificateFromDaoUpdated = giftCertificateDao.find(1L);
        GiftCertificate updatedCertificate = certificateFromDaoUpdated.get();
        assertEquals("Updated certificate", updatedCertificate.getName());
        assertEquals("Updated description", updatedCertificate.getDescription());
    }

    @Test
    void shouldSaveAndDeleteCertificate() {
        GiftCertificate certificate = GiftCertificate.builder()
                .name("New certificate")
                .description("Some description")
                .price(BigDecimal.valueOf(100D))
                .createDate(ZonedDateTime.now())
                .lastUpdateDate(ZonedDateTime.now())
                .duration(6)
                .tags(new ArrayList<>())
                .build();

        Long id = giftCertificateDao.save(certificate);
        Tag tag = Tag.builder()
                .id(2L)
                .name("Tag 4")
                .build();

        Long tagId = tagDao.save(tag);
        assertTrue(giftCertificateDao.find(id).isPresent());

        giftCertificateDao.delete(id);
        tagDao.delete(tagId);

        assertFalse(giftCertificateDao.find(id).isPresent());
    }


    @Test
    void shouldFindCertificateByPartOfDescription() {
        CertificateSearchQuery query = new CertificateSearchQuery();
        query.setPartOfDescription("Use the Apple Gift Ca");
        List<GiftCertificate> giftCertificates = giftCertificateDao.getCertificates(query, 1L, 10L);
        assertEquals(3, giftCertificates.size());
    }

    @Test
    void shouldFindCertificateByPartOfName() {
        CertificateSearchQuery query = new CertificateSearchQuery();
        query.setPartOfName("ple Gift Card $25");
        List<GiftCertificate> giftCertificates = giftCertificateDao.getCertificates(query, 1L, 10L);
        assertEquals(1L, giftCertificates.size());
    }
}