package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.TagDaoJdbc;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
        System.out.println(certificates);
        assertEquals(expectedListSize, certificates.size());
    }

    @Test
    void shouldFindCertificateById() {
        Long expectedId = 1L;
        GiftCertificate giftCertificate = certificateService.findCertificateById(expectedId);
        assertEquals(expectedId, giftCertificate.getId());
        assertThrows(GiftCertificateNotFoundException.class, () -> {
            certificateService.findCertificateById(12L);
        });
    }

    @Test
    void shouldSaveAndRemoveCertificate() {
        GiftCertificate certificate = GiftCertificate.builder()
                .name("New certificate")
                .description("Some description")
                .price(BigDecimal.valueOf(100D))
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .duration(6)
                .tags(new ArrayList<>())
                .build();

        System.out.println(certificate);

        Long certificateId = certificateService.saveCertificate(certificate);
        certificateService.addTagToCertificate(certificateId, 3L);

        GiftCertificate newCertificate = certificateService.findCertificateById(certificateId);
        System.out.println(newCertificate);

        assertTrue(certificateService.findCertificateById(certificateId).getName().contains("New certificate"));

        certificateService.removeTagFromCertificate(certificateId, 1L);
        certificateService.deleteCertificate(certificateId);

        assertThrows(GiftCertificateNotFoundException.class, () -> {
            certificateService.findCertificateById(certificateId);
        });

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