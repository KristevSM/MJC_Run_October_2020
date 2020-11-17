package com.epam.esm.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.ValidationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.constants.AppConstants.DEFAULT_PAGE_NUMBER;
import static com.epam.esm.constants.AppConstants.DEFAULT_PAGE_SIZE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Sergei Kristev
 *
 * Gets data from rest in JSON format on path "/gift-certificates".
 */
@RestController
@RequestMapping("/gift-certificates")
public class TagController {

    private final TagService tagService;

    /**
     * Accepts service layer objects and tag validator.
     *
     * @param tagService            TagService instance.
     */
    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Gets list of all tags.
     *
     * @param page  page's number
     * @param pageSize page size
     * @return Tags list.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/tags", produces = {"application/hal+json"})
    public CollectionModel<Tag> findAllTags(@RequestParam(value = "page") Optional<Long> page,
                                             @RequestParam(value = "page_size") Optional<Long> pageSize) {
        Long pageNumber = page.orElse(DEFAULT_PAGE_NUMBER);
        Long pageSizeNumber = pageSize.orElse(DEFAULT_PAGE_SIZE);

        ValidationUtils.checkPaginationData(pageNumber, pageSizeNumber);

        List<Tag> tagList = tagService.findAllTags(pageNumber, pageSizeNumber);
        for (Tag tag : tagList) {
            Link selfLink = linkTo(methodOn(TagController.class)
                    .findTagById(tag.getId())).withSelfRel();
            tag.add(selfLink);
        }
        Link link = linkTo(TagController.class).slash("tags").withSelfRel();
        return new CollectionModel<>(tagList, link);    }

    /**
     * Gets tag by id.
     *
     * @param id Tag id.
     * @return Tag instance.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "tags/{id}", produces = {"application/hal+json"})
    public Tag findTagById(@PathVariable Long id) {
        Tag tag = tagService.findTagById(id);
        Link selfLink = linkTo(methodOn(TagController.class)
                .findTagById(tag.getId())).withSelfRel();
        Link tagsLink = linkTo(methodOn(TagController.class)
                .findAllTags(Optional.of(DEFAULT_PAGE_NUMBER), Optional.of(DEFAULT_PAGE_SIZE))).withRel("tags");
        tag.add(tagsLink);
        tag.add(selfLink);
        return tag;
    }

    /**
     * Adds new tag.
     *
     * The tag is saved through the <i>tagService</i>.
     * Is returning <i>ResponseEntity</i> with <i>HttpStatus.CREATED</i>.
     *
     * @param tag             Tag instance.
     * @param ucBuilder       UriComponentsBuilder instance.
     * @return ResponseEntity.
     */
    @PostMapping(path = "/tags", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Tag> addTag(@RequestBody @Valid Tag tag,
                                      UriComponentsBuilder ucBuilder) {

            Long tagId = tagService.saveTag(tag);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/tags/{id}").buildAndExpand(tagId).toUri());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Updates tag.
     *
     * First, finds a tag by ID. Subsequently, if the tag record is found, invokes
     * the applyPatchToTag(patch, tag) method. Then applies the JsonPatch to the tag.
     * After that the tag is updated through the <i>tagService</i>. Is returning <i>ResponseEntity</i> with <i>HttpStatus.CREATED</i>.
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

                tagService.updateTag(tagPatched);
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(ucBuilder.path("/tags/{id}").buildAndExpand(tagPatched.getId()).toUri());
                return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

    /**
     * Gets tag by id.
     * Get the most widely used tag of a user with the highest cost of all orders.
     * @return Tag instance.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/tags/popular", produces = {"application/hal+json"})
    public Tag getUsersMostWidelyUsedTag() {
        Tag tag = tagService.getUsersMostWidelyUsedTag();
        Link selfLink = linkTo(methodOn(TagController.class)
                .findTagById(tag.getId())).withSelfRel();
        Link tagsLink = linkTo(methodOn(TagController.class)
                .findAllTags(Optional.of(DEFAULT_PAGE_NUMBER), Optional.of(DEFAULT_PAGE_SIZE))).withRel("tags");
        tag.add(tagsLink);
        tag.add(selfLink);
        return tag;
    }
}
