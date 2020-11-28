package com.epam.esm.service;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        GiftCertificate certificate1 = giftCertificateService.findCertificateById(1L);
        GiftCertificate certificate2 = giftCertificateService.findCertificateById(2L);
        assertEquals(1L, certificate1.getId());
        assertEquals(2L, certificate2.getId());
    }
}