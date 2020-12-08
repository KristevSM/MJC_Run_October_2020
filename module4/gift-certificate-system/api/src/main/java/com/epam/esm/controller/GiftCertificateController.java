package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.repository.CertificateSearchQuery;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.CertificateSearchValidator;
import com.epam.esm.validator.ValidationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.constants.AppConstants.DEFAULT_PAGE_NUMBER;
import static com.epam.esm.constants.AppConstants.DEFAULT_PAGE_SIZE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Sergei Kristev
 * <p>
 * Gets data from rest in JSON format on path "/gift-certificates".
 */
@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final CertificateSearchValidator searchValidator;

    /**
     * Constructor accepts service layer objects and certificate validator.
     *
     * @param giftCertificateService GiftCertificateService instance.
     * @param searchValidator        CertificateSearchValidator instance.
     */
    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService,
                                     CertificateSearchValidator searchValidator) {
        this.giftCertificateService = giftCertificateService;
        this.searchValidator = searchValidator;
    }

    /**
     * Adds new gift certificate.
     * <p>
     * The certificate saved from <i>giftCertificateService</i>. Default tag "Main" is set for the certificate.
     * If new tags are passed during creation certificate, they saved throw <i>tagService</i>Is returning
     * <i>ResponseEntity</i> with <i>HttpStatus.CREATED</i>.
     *
     * @param giftCertificateDTO GiftCertificate instance.
     * @return ResponseEntity.
     */
    @PostMapping(path = "/certificates", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDTO addGiftCertificate(@RequestBody @Valid GiftCertificateDTO giftCertificateDTO) {
        return giftCertificateService.saveCertificate(giftCertificateDTO);
    }

    /**
     * Updates gift certificate.
     * <p>
     * First, finds a certificate by ID. Subsequently, if the certificate record is found, invokes
     * the applyPatchToGiftCertificate(patch, giftCertificate) method. Then applies the JsonPatch to the certificate.
     * Is returning <i>ResponseEntity</i> with <i>HttpStatus.CREATED</i>.
     *
     * @param id        GiftCertificate id.
     * @param patch     JsonPatch.
     * @return ResponseEntity.
     */
    @PatchMapping(path = "certificates/{id}", consumes = "application/json-patch+json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GiftCertificateDTO> updateGiftCertificate(@PathVariable Long id,
                                                                    @RequestBody JsonPatch patch) {
        try {
            GiftCertificateDTO oldCertificate = giftCertificateService.findCertificateById(id);
            GiftCertificateDTO certificatePatched = applyPatchToGiftCertificate(patch, oldCertificate);
            GiftCertificateDTO updatedCertificate = giftCertificateService.updateCertificate(certificatePatched);
            return new ResponseEntity<>(updatedCertificate, HttpStatus.CREATED);

        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes gift certificate by id.
     *
     * @param id GiftCertificate id.
     * @return ResponseEntity.
     */
    @DeleteMapping(path = "certificates/{id}", produces = {"application/hal+json"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @GetMapping(value = "certificates/{id}", produces = {"application/hal+json"})
    public GiftCertificateDTO findCertificateById(@PathVariable Long id) {
        GiftCertificateDTO certificateDTO = giftCertificateService.findCertificateById(id);
        List<TagDTO> tags = certificateDTO.getTags();
        tags.forEach(tag -> {
            Link selfLink = linkTo(methodOn(TagController.class)
                    .findTagById(tag.getId())).withSelfRel();
            tag.add(selfLink);
        });
        Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                .findCertificateById(certificateDTO.getId())).withSelfRel();
        certificateDTO.add(selfLink);
        return certificateDTO;
    }

    /**
     * Searches gift certificates.
     * <p>
     * First, creates an instance of CertificateSearchQuery. Then method checks if the input parameters required for searching
     * and sorting certificates are not empty, sets their values to the queue object, validates and passes it to the giftCertificateService.
     *
     * @param tagName           value of "tag_name"
     * @param partOfName        value of "part_of_name"
     * @param partOfDescription value of "part_of_description"
     * @param sortParameter     value of "sort"
     * @param sortOrder         value of "sort_order"
     * @param page              page's number
     * @param pageSize          page size
     * @return GiftCertificates list.
     */
    @GetMapping(value = "/certificates", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<GiftCertificateDTO> findCertificates(@RequestParam(value = "tag_name") Optional<String> tagName,
                                                                @RequestParam(value = "part_of_name") Optional<String> partOfName,
                                                                @RequestParam(value = "part_of_description") Optional<String> partOfDescription,
                                                                @RequestParam(value = "sort") Optional<String> sortParameter,
                                                                @RequestParam(value = "sort_order") Optional<String> sortOrder,
                                                                @RequestParam(value = "page") Optional<Integer> page,
                                                                @RequestParam(value = "page_size") Optional<Integer> pageSize
    ) {
        int pageNumber = page.orElse(DEFAULT_PAGE_NUMBER);
        int pageSizeNumber = pageSize.orElse(DEFAULT_PAGE_SIZE);

        ValidationUtils.checkPaginationData(pageNumber, pageSizeNumber);

        CertificateSearchQuery query = new CertificateSearchQuery();
        tagName.ifPresent(query::setTagName);
        partOfName.ifPresent(query::setPartOfName);
        partOfDescription.ifPresent(query::setPartOfDescription);
        sortParameter.ifPresent(query::setSortParameter);
        sortOrder.ifPresent(query::setSortOrder);

        BindingResult result = new BeanPropertyBindingResult(query, "searchQuery");
        searchValidator.validate(query, result);
        if (result.hasErrors()) {
            String brokenField = result.getFieldErrors().get(0).getField();
            String errorCode = result.getFieldErrors().get(0).getCode();
            throw new InvalidInputDataException(MessageFormat.format("Unexpected certificate''s field: {0}, error code: {1}",
                    brokenField, errorCode));
        }
        Page<GiftCertificateDTO> certificateDTOPage
                = giftCertificateService.getCertificates(query, pageNumber - 1, pageSizeNumber);

        for (GiftCertificateDTO certificate : certificateDTOPage) {
            Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                    .findCertificateById(certificate.getId())).withSelfRel();
            certificate.add(selfLink);
        }
        Link link = linkTo(GiftCertificateController.class).slash("certificates").withSelfRel();

        CollectionModel<GiftCertificateDTO> collectionModel = new CollectionModel(certificateDTOPage, link);

        if (pageNumber > 1) {
            Link previousPage = linkTo(methodOn(GiftCertificateController.class)
                    .findCertificates(tagName, partOfName, partOfDescription, sortParameter, sortOrder, Optional.of(pageNumber - 1), Optional.of(pageSizeNumber))).withRel("previousPage");
            collectionModel.add(previousPage);
        }
        if (pageNumber < certificateDTOPage.getTotalPages()) {
            Link nextPage = linkTo(methodOn(GiftCertificateController.class)
                    .findCertificates(tagName, partOfName, partOfDescription, sortParameter, sortOrder, Optional.of(pageNumber - 1), Optional.of(pageSizeNumber))).withRel("previousPage");
            collectionModel.add(nextPage);
        }
        return collectionModel;
    }

    /**
     * Applies JsonPatch to certificate.
     * <p>
     * This method applies the JsonPatch to the certificate.
     *
     * @param patch             JsonPatch
     * @param targetCertificate GiftCertificate instance
     * @return patched GiftCertificate instance.
     */
    private GiftCertificateDTO applyPatchToGiftCertificate(JsonPatch patch, GiftCertificateDTO targetCertificate)
            throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JsonNode patched = patch.apply(objectMapper.convertValue(targetCertificate, JsonNode.class));
        return objectMapper.treeToValue(patched, GiftCertificateDTO.class);
    }


    /**
     * Searches gift certificates by several tags.
     * <p>
     * Searches gift certificates by several tags.
     *
     * @param tagNames list of tag names
     * @param page     page's number
     * @param pageSize page size
     * @return GiftCertificates list.
     */
    @GetMapping(value = "/certificates/search", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<GiftCertificateDTO> findCertificates(@RequestParam(value = "tag_name", defaultValue = "") List<String> tagNames,
                                                                @RequestParam(value = "page") Optional<Integer> page,
                                                                @RequestParam(value = "page_size") Optional<Integer> pageSize
    ) {
        if (tagNames.isEmpty()) {
            throw new InvalidInputDataException("Search query mustn't be empty");
        }
        int pageNumber = page.orElse(DEFAULT_PAGE_NUMBER);
        int pageSizeNumber = pageSize.orElse(DEFAULT_PAGE_SIZE);

        ValidationUtils.checkPaginationData(pageNumber, pageSizeNumber);

        Page<GiftCertificateDTO> certificates = giftCertificateService.findCertificatesByTags(tagNames, pageNumber - 1, pageSizeNumber);
        for (GiftCertificateDTO certificate : certificates) {
            Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                    .findCertificateById(certificate.getId())).withSelfRel();
            certificate.add(selfLink);
        }
        Link link = linkTo(GiftCertificateController.class).slash("certificates").withSelfRel();
        CollectionModel<GiftCertificateDTO> collectionModel = new CollectionModel(certificates, link);

        if (pageNumber > 1) {
            Link previousPage = linkTo(methodOn(GiftCertificateController.class)
                    .findCertificates(tagNames, Optional.of(pageNumber - 1), Optional.of(pageSizeNumber))).withRel("previousPage");
            collectionModel.add(previousPage);
        }
        if (pageNumber < certificates.getTotalPages()) {
            Link nextPage = linkTo(methodOn(GiftCertificateController.class)
                    .findCertificates(tagNames, Optional.of(pageNumber - 1), Optional.of(pageSizeNumber))).withRel("previousPage");
            collectionModel.add(nextPage);
        }
        return collectionModel;
    }

    /**
     * Change single field of gift certificate
     * <p>
     * Change single field of gift certificate
     *
     * @param id         certificate's id
     * @param fieldName  updating field's name
     * @param fieldValue field's value
     * @return GiftCertificate instance.
     */
    @PostMapping(path = "/certificates/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDTO updateSingleCertificateField(@PathVariable Long id,
                                                           @RequestParam(value = "fieldName", defaultValue = "") String fieldName,
                                                           @RequestParam(value = "fieldValue", defaultValue = "") String fieldValue) {

        GiftCertificateDTO certificate = giftCertificateService.updateSingleCertificateField(id, fieldName, fieldValue);
        List<TagDTO> tags = certificate.getTags();
        tags.forEach(tag -> {
            Link selfLink = linkTo(methodOn(TagController.class)
                    .findTagById(tag.getId())).withRel("tag");
            tag.add(selfLink);
        });
        Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                .findCertificateById(certificate.getId())).withRel("certificate");
        certificate.add(selfLink);
        return certificate;
    }
}
