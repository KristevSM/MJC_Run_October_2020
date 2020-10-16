package com.epam.esm.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GiftCertificateController {

    @Autowired
    private GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping(value = "/gift-certificates")
    public List<GiftCertificate> findAll() {
        return giftCertificateService.findAllCertificates();
    }
}
