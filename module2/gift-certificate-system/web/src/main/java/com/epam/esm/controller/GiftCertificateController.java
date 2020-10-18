package com.epam.esm.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateValidator certificateValidator;
    private final TagService tagService;
    private final TagValidator tagValidator;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateValidator certificateValidator,
                                     TagService tagService, TagValidator tagValidator) {
        this.giftCertificateService = giftCertificateService;
        this.certificateValidator = certificateValidator;
        this.tagService = tagService;
        this.tagValidator = tagValidator;
    }



    @GetMapping(value = "/certificates")
    public List<GiftCertificate> findAll() {
        return giftCertificateService.findAllCertificates();
    }

    @PostMapping(path = "/certificates", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Integer> addGiftCertificate(@RequestBody @Valid GiftCertificate giftCertificate, BindingResult result) {
        if (giftCertificate.getTags() == null) {
           giftCertificate.setTags(new ArrayList<>());
        }
        certificateValidator.validate(giftCertificate, result);
        if (result.hasErrors()) {
            return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        } else {
            Long certificateId = giftCertificateService.saveCertificate(giftCertificate);
            //todo create external tag properties
            tagService.assignDefaultTag("Main", certificateId);
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping(value = "certificates/{id}")
    public GiftCertificate findById(@PathVariable Long id) {
        //todo create new exception
        return giftCertificateService.findCertificateById(id).orElseThrow(NoSuchElementException::new);
    }

}
