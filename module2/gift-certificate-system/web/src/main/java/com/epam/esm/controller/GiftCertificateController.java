package com.epam.esm.controller;

import com.epam.esm.dao.CertificateSearchQuery;
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

/**
 * @author Sergei Kristev
 *
 * Gets data from rest in JSON format on path "/gift-certificates".
 */
@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateValidator certificateValidator;
    private final TagService tagService;

    /**
     * Constructor accepts service layer objects and certificate validator.
     *
     * @param giftCertificateService GiftCertificateService instance.
     * @param certificateValidator   GiftCertificateValidator instance.
     * @param tagService             TagService instance.
     */
    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateValidator certificateValidator,
                                     TagService tagService) {
        this.giftCertificateService = giftCertificateService;
        this.certificateValidator = certificateValidator;
        this.tagService = tagService;
    }

    /**
     * Adds new gift certificate.
     *
     * The certificate object is being validated. The date of certificate creation and last update are set
     * to the current time. If there are invalid fields, it is returned <i>ResponseEntity</i> with <i>HttpStatus.BAD_REQUEST</i>
     * and error's data. If successful, the certificate is saved through the <i>giftCertificateService</i> and the
     * default tag "Main" is set for the certificate. Is returning <i>ResponseEntity</i> with <i>HttpStatus.CREATED</i>.
     *
     * @param giftCertificate GiftCertificate instance.
     * @param ucBuilder       UriComponentsBuilder instance.
     * @return ResponseEntity.
     */
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

    /**
     * Updates gift certificate.
     *
     * First, finds a certificate by ID. Subsequently, if the certificate record is found, invokes
     * the applyPatchToGiftCertificate(patch, giftCertificate) method. Then applies the JsonPatch to the certificate.
     * The certificate object is being validated. If there are invalid fields, it is returned <i>ResponseEntity</i>
     * with <i>HttpStatus.BAD_REQUEST</i> and error's data. If successful, the certificate is updated through
     * the <i>giftCertificateService</i>. Is returning <i>ResponseEntity</i> with <i>HttpStatus.CREATED</i>.
     *
     * @param id        GiftCertificate id.
     * @param patch     JsonPatch.
     * @param ucBuilder UriComponentsBuilder instance.
     * @return ResponseEntity.
     */
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

    /**
     * Deletes gift certificate bu id.
     *
     * @param id GiftCertificate id.
     * @return ResponseEntity.
     */
    @DeleteMapping(path = "certificates/{id}")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable Long id) {

        giftCertificateService.deleteCertificate(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Gets gift certificate by id.
     *
     * @param id GiftCertificate id.
     * @return GiftCertificate instance.
     */
    @GetMapping(value = "certificates/{id}")
    public GiftCertificate findCertificateById(@PathVariable Long id) {
        return giftCertificateService.findCertificateById(id);
    }

    /**
     * Searches gift certificates.
     *
     * First, creates an instance of CertificateSearchQuery. Then method checks if the input parameters required for searching
     * and sorting certificates are not empty, sets their values to the queue object, and passes it to the giftCertificateService.
     *
     * @param tagName           value of "tag_name"
     * @param partOfName        value of "part_of_name"
     * @param partOfDescription value of "part_of_description"
     * @param sortParameter     value of "sort"
     * @param sortOrder         value of "sort_order"
     * @return GiftCertificates list.
     */
    @GetMapping(value = "/search")
    public List<GiftCertificate> findCertificates(@RequestParam(value = "tag_name") Optional<String> tagName,
                                                  @RequestParam(value = "part_of_name") Optional<String> partOfName,
                                                  @RequestParam(value = "part_of_description") Optional<String> partOfDescription,
                                                  @RequestParam(value = "sort") Optional<String> sortParameter,
                                                  @RequestParam(value = "sort_order") Optional<String> sortOrder) {
        CertificateSearchQuery query = new CertificateSearchQuery();

        tagName.ifPresent(query::setTagName);
        partOfName.ifPresent(query::setPartOfName);
        partOfDescription.ifPresent(query::setPartOfDescription);
        sortParameter.ifPresent(query::setSortParameter);
        sortOrder.ifPresent(query::setSortOrder);

        return giftCertificateService.getCertificates(query);
    }

    /**
     * Applies JsonPatch to certificate.
     *
     * This method applies the JsonPatch to the certificate.
     *
     * @param patch             JsonPatch
     * @param targetCertificate GiftCertificate instance
     * @return patched GiftCertificate instance.
     */
    private GiftCertificate applyPatchToGiftCertificate(JsonPatch patch, GiftCertificate targetCertificate)
            throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JsonNode patched = patch.apply(objectMapper.convertValue(targetCertificate, JsonNode.class));
        return objectMapper.treeToValue(patched, GiftCertificate.class);
    }

}
