package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.TagDaoJdbc;
import com.epam.esm.model.GiftCertificate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:test-db.xml"})
class GiftCertificateServiceImplTest {

    private GiftCertificateService certificateService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @BeforeEach
    void setUp() {
        certificateService = new GiftCertificateServiceImpl(giftCertificateDao, tagDao);
        tagDao = new TagDaoJdbc(dataSource);
    }
    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldFindAllCertificates() {
        List<GiftCertificate> certificates = certificateService.findAllCertificates();
        int expectedListSize = 3;
        assertEquals(expectedListSize, certificates.size());
    }

    @Test
    void findCertificateById() {
    }

    @Test
    void saveCertificate() {
    }

    @Test
    void updateCertificate() {
    }

    @Test
    void deleteCertificate() {
    }

    @Test
    void getCertificatesByTagName() {
    }

    @Test
    void getCertificatesByPartOfName() {
    }

    @Test
    void getCertificatesByPartOfDescription() {
    }

    @Test
    void addTagToCertificate() {
    }

    @Test
    void removeTagFromCertificate() {
    }

    @Test
    void sortCertificateByParameters() {
    }
}