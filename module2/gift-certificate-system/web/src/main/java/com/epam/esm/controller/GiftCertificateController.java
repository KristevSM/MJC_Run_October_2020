package com.epam.esm.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateValidator certificateValidator;
    private final TagValidator tagValidator;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateValidator certificateValidator, TagValidator tagValidator) {
        this.giftCertificateService = giftCertificateService;
        this.certificateValidator = certificateValidator;
        this.tagValidator = tagValidator;
    }

    @Autowired


    @GetMapping(value = "/gift-certificates")
    public List<GiftCertificate> findAll() {
        return giftCertificateService.findAllCertificates();
    }

    @PostMapping(path = "/gift-certificates", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Integer> addGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        giftCertificateService.saveCertificate(giftCertificate);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}")
    public GiftCertificate findById(@PathVariable Long id) {
        //todo create new exception
        return giftCertificateService.findCertificateById(id).orElseThrow(NoSuchElementException::new);
    }

}
