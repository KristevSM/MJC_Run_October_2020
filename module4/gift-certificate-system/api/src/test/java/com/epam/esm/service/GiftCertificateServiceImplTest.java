package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableConfigurationProperties
class GiftCertificateServiceImplTest {

    @Autowired
    private GiftCertificateService giftCertificateService;

    @Test
    void shouldFindCertificateById() {
        GiftCertificateDTO certificate1 = giftCertificateService.findCertificateById(1L);
        GiftCertificateDTO certificate2 = giftCertificateService.findCertificateById(2L);
        assertEquals(1L, certificate1.getId());
        assertEquals(2L, certificate2.getId());
    }
}