package com.epam.esm.controller;

import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
     * @return Tags list.
     */
    @GetMapping(value = "/tags")
    public List<Tag> findAllTags(@RequestParam(value = "from") Optional<Integer> from,
                                 @RequestParam(value = "page_size") Optional<Integer> pages) {
        int fromTag = from.orElse(0);
        int pageSize = pages.orElse(20);
        return tagService.findAllTags(fromTag, pageSize);
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

    @GetMapping(value = "/tags/popular")
    public Tag getUsersMostWidelyUsedTag() {
        return tagService.getUsersMostWidelyUsedTag();
    }
}
