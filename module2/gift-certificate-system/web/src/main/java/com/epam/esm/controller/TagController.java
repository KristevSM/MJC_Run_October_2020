package com.epam.esm.controller;

import com.epam.esm.dao.CertificateSearchQuery;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;

/**
 * @author Sergei Kristev
 *
 * Gets data from rest in JSON format on path "/gift-certificates".
 */
@RestController
@RequestMapping("/gift-certificates")
public class TagController {

    private final TagService tagService;
    private final GiftCertificateService certificateService;
    private final TagValidator tagValidator;

    /**
     * Accepts service layer objects and tag validator.
     *
     * @param tagService            TagService instance.
     * @param certificateService    GiftCertificateService instance.
     * @param tagValidator          TagValidator instance.
     */
    @Autowired
    public TagController(TagService tagService, GiftCertificateService certificateService, TagValidator tagValidator) {
        this.tagService = tagService;
        this.certificateService = certificateService;
        this.tagValidator = tagValidator;
    }

    /**
     * Gets list of all tags.
     *
     * @return Tags list.
     */
    @GetMapping(value = "/tags")
    public List<Tag> findAllTags() {
        return tagService.findAllTags();
    }

    /**
     * Gets tag by id.
     *
     * @param id Tag id.
     * @return Tag instance.
     */
    @GetMapping(value = "tags/{id}")
    public Tag findTagById(@PathVariable Long id) {
        return tagService.findTagById(id);
    }

    /**
     * Adds new tag.
     *
     * The tag object is being validated. If there are invalid fields, it is returned <i>ResponseEntity</i> with
     * <i>HttpStatus.BAD_REQUEST</i> and error's data. If successful, the tag is saved through the <i>tagService</i>.
     * Is returning <i>ResponseEntity</i> with <i>HttpStatus.CREATED</i>.
     *
     * @param tag             Tag instance.
     * @param ucBuilder       UriComponentsBuilder instance.
     * @return ResponseEntity.
     */
    @PostMapping(path = "/tags", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Tag> addTag(@RequestBody @Valid Tag tag,
                                      UriComponentsBuilder ucBuilder) {

        BindingResult result = new BeanPropertyBindingResult(tag, "tag");
        tagValidator.validate(tag, result);
        if (result.hasErrors()) {
            return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        } else {
            Long tagId = tagService.saveTag(tag);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/tags/{id}").buildAndExpand(tagId).toUri());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
    }

    /**
     * Updates tag.
     *
     * First, finds a tag by ID. Subsequently, if the tag record is found, invokes
     * the applyPatchToTag(patch, tag) method. Then applies the JsonPatch to the tag.
     * The tag object is being validated. If there are invalid fields, it is returned <i>ResponseEntity</i>
     * with <i>HttpStatus.BAD_REQUEST</i> and error's data. If successful, the tag is updated through
     * the <i>tagService</i>. Is returning <i>ResponseEntity</i> with <i>HttpStatus.CREATED</i>.
     *
     * @param id        Tag id.
     * @param patch     JsonPatch.
     * @param ucBuilder UriComponentsBuilder instance.
     * @return ResponseEntity.
     */
    @PatchMapping(path = "tags/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Tag> updateTag(@PathVariable Long id,
                                         @RequestBody JsonPatch patch,
                                         UriComponentsBuilder ucBuilder) {
        try {
            Tag oldTag = tagService.findTagById(id);
            Tag tagPatched = applyPatchToTag(patch, oldTag);
            BindingResult result = new BeanPropertyBindingResult(tagPatched, "tagPatched");
            tagValidator.validate(tagPatched, result);
            if (result.hasErrors()) {
                return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            } else {
                tagService.updateTag(tagPatched);
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(ucBuilder.path("/tags/{id}").buildAndExpand(tagPatched.getId()).toUri());
                return new ResponseEntity<>(headers, HttpStatus.CREATED);
            }
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Deletes tag by id.
     *
     * @param id Tag id.
     * @return ResponseEntity.
     */
    @DeleteMapping(path = "tags/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        Tag tag = tagService.findTagById(id);
        try {
            CertificateSearchQuery query = new CertificateSearchQuery();
            query.setTagName(tag.getName());
            List<GiftCertificate> certificates = certificateService.getCertificates(query);
            certificates.forEach(certificate -> certificateService.removeTagFromCertificate(certificate.getId(), id));
        } catch (GiftCertificateNotFoundException e) {
            //logging
        }
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Applies JsonPatch to tag.
     *
     * This method applies the JsonPatch to the tag.
     *
     * @param patch             JsonPatch
     * @param targetTag         Tag instance
     * @return patched Tag instance.
     */
    private Tag applyPatchToTag(
            JsonPatch patch, Tag targetTag) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        JsonNode patched = patch.apply(objectMapper.convertValue(targetTag, JsonNode.class));
        return objectMapper.treeToValue(patched, Tag.class);
    }
}
