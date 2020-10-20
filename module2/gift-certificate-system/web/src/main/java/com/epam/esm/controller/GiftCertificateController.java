package com.epam.esm.controller;

import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.GiftCertificateValidator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;


import java.time.ZonedDateTime;
import java.util.*;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateValidator certificateValidator;
    private final TagService tagService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateValidator certificateValidator,
                                     TagService tagService) {
        this.giftCertificateService = giftCertificateService;
        this.certificateValidator = certificateValidator;
        this.tagService = tagService;
    }


    @PostMapping(path = "/certificates", consumes = "application/json", produces = "application/json")
    public ResponseEntity<GiftCertificate> addGiftCertificate(@RequestBody @Valid GiftCertificate giftCertificate,
                                                              UriComponentsBuilder ucBuilder) {

        if (giftCertificate.getTags() == null) {
            giftCertificate.setTags(new ArrayList<>());
        }

        giftCertificate.setCreateDate(ZonedDateTime.now());
        giftCertificate.setLastUpdateDate(ZonedDateTime.now());

        BindingResult result = new BeanPropertyBindingResult(giftCertificate, "giftCertificate");
        certificateValidator.validate(giftCertificate, result);
        if (result.hasErrors()) {
            return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        } else {
            Long certificateId = giftCertificateService.saveCertificate(giftCertificate);
            tagService.assignDefaultTag("Main", certificateId);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/certificates/{id}").buildAndExpand(certificateId).toUri());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
    }

    @PatchMapping(path = "certificates/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<GiftCertificate> updateGiftCertificate(@PathVariable Long id,
                                                                 @RequestBody JsonPatch patch,
                                                                 UriComponentsBuilder ucBuilder) {

        try {
            GiftCertificate oldCertificate = giftCertificateService.findCertificateById(id);
            GiftCertificate certificatePatched = applyPatchToGiftCertificate(patch, oldCertificate);

            BindingResult result = new BeanPropertyBindingResult(certificatePatched, "giftCertificate");
            certificateValidator.validate(certificatePatched, result);
            if (result.hasErrors()) {
                return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            } else {
                giftCertificateService.updateCertificate(certificatePatched);
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(ucBuilder.path("/certificates/{id}").buildAndExpand(certificatePatched.getId()).toUri());
                return new ResponseEntity<>(headers, HttpStatus.CREATED);
            }
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (GiftCertificateNotFoundException | TagNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(path = "certificates/{id}")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable Long id) {

        giftCertificateService.deleteCertificate(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = "certificates/{id}")
    public GiftCertificate findCertificateById(@PathVariable Long id) {
        return giftCertificateService.findCertificateById(id);
    }

    @GetMapping(value = "/certificates")
    public List<GiftCertificate> findAllCertificates(@RequestParam(value = "sort_name") Optional<String> nameSortDirection,
                                         @RequestParam(value = "sort_create_date") Optional<String> createDateDirection) {
        List<GiftCertificate> certificates = giftCertificateService.findAllCertificates();
        if (nameSortDirection.isPresent()) {
            return giftCertificateService.sortCertificateByParameters("sort_name", nameSortDirection.get(), certificates);
        } else if (createDateDirection.isPresent()) {
            return giftCertificateService.sortCertificateByParameters("sort_create_date", createDateDirection.get(), certificates);
        } else {
            return certificates;
        }
    }

    @GetMapping(value = "/search")
    public List<GiftCertificate> findCertificateByParameters(@RequestParam(value = "tag_name") Optional<String> tagName,
                                                             @RequestParam(value = "part_of_name") Optional<String> partOfName,
                                                             @RequestParam(value = "part_of_description") Optional<String> partOfDescription) {

        if (tagName.isPresent()) {
            return giftCertificateService.getCertificatesByTagName(tagName.get());
        } else if (partOfName.isPresent()) {
            return giftCertificateService.getCertificatesByPartOfName(partOfName.get());
        } else if (partOfDescription.isPresent()) {
            return giftCertificateService.getCertificatesByPartOfDescription(partOfDescription.get());
        } else {
            return giftCertificateService.findAllCertificates();
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

}
