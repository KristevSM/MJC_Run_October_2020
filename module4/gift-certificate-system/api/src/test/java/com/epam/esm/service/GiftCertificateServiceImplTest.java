package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GiftCertificateServiceImplTest {

    @Autowired
    private GiftCertificateService giftCertificateService;

//    @Test
//    public void shouldFindAllCertificates() {
//        List<Order> orders = giftCertificateService.getCertificates(1L, 20L);
//        assertEquals(5L, orders.size());
//    }

    @Test
    void shouldFindCertificateById() {
        GiftCertificateDTO certificate1 = giftCertificateService.findCertificateById(1L);
        GiftCertificateDTO certificate2 = giftCertificateService.findCertificateById(2L);
        assertEquals(1L, certificate1.getId());
        assertEquals(2L, certificate2.getId());
    }
}