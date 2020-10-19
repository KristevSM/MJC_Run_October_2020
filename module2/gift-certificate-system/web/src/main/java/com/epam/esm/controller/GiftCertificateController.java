package com.epam.esm.controller;

import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.TagValidator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<Integer> addGiftCertificate(@RequestBody @Valid GiftCertificate giftCertificate) {

        if (giftCertificate.getTags() == null) {
            giftCertificate.setTags(new ArrayList<>());
        }
        BindingResult result = new BeanPropertyBindingResult(giftCertificate, "giftCertificate");
        certificateValidator.validate(giftCertificate, result);
        if (result.hasErrors()) {
            return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        } else {
            Long certificateId = giftCertificateService.saveCertificate(giftCertificate);
            tagService.assignDefaultTag("Main", certificateId);
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping(path = "certificates/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<GiftCertificate> updateGiftCertificate(@PathVariable Long id,
                                                                 @RequestBody JsonPatch patch) {

        try {
            GiftCertificate oldCertificate = giftCertificateService.findCertificateById(id);
            GiftCertificate certificatePatched = applyPatchToGiftCertificate(patch, oldCertificate);

            BindingResult result = new BeanPropertyBindingResult(certificatePatched, "giftCertificate");
            certificateValidator.validate(certificatePatched, result);
            if (result.hasErrors()) {
                return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            } else {
                giftCertificateService.updateCertificate(certificatePatched);
                return ResponseEntity.ok().build();
            }
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (GiftCertificateNotFoundException | TagNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private GiftCertificate applyPatchToGiftCertificate(
            JsonPatch patch, GiftCertificate targetCertificate) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JsonNode patched = patch.apply(objectMapper.convertValue(targetCertificate, JsonNode.class));
        return objectMapper.treeToValue(patched, GiftCertificate.class);
    }


    @GetMapping(value = "certificates/{id}")
    public GiftCertificate findById(@PathVariable Long id) {
        return giftCertificateService.findCertificateById(id);
    }


}
