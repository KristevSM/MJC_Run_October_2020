package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
    public CollectionModel<TagDTO> findAllTags(@RequestParam(value = "page") Optional<Integer> page,
                                               @RequestParam(value = "page_size") Optional<Integer> pageSize) {
        int pageNumber = page.orElse(DEFAULT_PAGE_NUMBER);
        int pageSizeNumber = pageSize.orElse(DEFAULT_PAGE_SIZE);

        ValidationUtils.checkPaginationData(pageNumber, pageSizeNumber);

        List<TagDTO> tagDTOList = tagService.findAllTags(pageNumber-1, pageSizeNumber);
        for (TagDTO tagDTO : tagDTOList) {
            Link selfLink = linkTo(methodOn(TagController.class)
                    .findTagById(tagDTO.getId())).withSelfRel();
            tagDTO.add(selfLink);
        }
        Link link = linkTo(TagController.class).slash("tags").withSelfRel();
        return new CollectionModel<>(tagDTOList, link);    }

    /**
     * Gets tag by id.
     *
     * @param id Tag id.
     * @return Tag instance.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "tags/{id}", produces = {"application/hal+json"})
    public TagDTO findTagById(@PathVariable Long id) {
        TagDTO tagDTO = tagService.findTagById(id);
        Link selfLink = linkTo(methodOn(TagController.class)
                .findTagById(tagDTO.getId())).withSelfRel();
        Link tagsLink = linkTo(methodOn(TagController.class)
                .findAllTags(Optional.of(DEFAULT_PAGE_NUMBER), Optional.of(DEFAULT_PAGE_SIZE))).withRel("tags");
        tagDTO.add(tagsLink);
        tagDTO.add(selfLink);
        return tagDTO;
    }

    /**
     * Adds new tag.
     *
     * The tag is saved through the <i>tagService</i>.
     * Is returning <i>ResponseEntity</i> with <i>HttpStatus.CREATED</i>.
     *
     * @param tagDTO             Tag instance.
     * @param ucBuilder       UriComponentsBuilder instance.
     * @return ResponseEntity.
     */
    @PostMapping(path = "/tags", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TagDTO> addTag(@RequestBody @Valid TagDTO tagDTO,
                                      UriComponentsBuilder ucBuilder) {

            Long tagId = tagService.saveTag(tagDTO);
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TagDTO> updateTag(@PathVariable Long id,
                                         @RequestBody JsonPatch patch,
                                         UriComponentsBuilder ucBuilder) {
        try {
            TagDTO oldTag = tagService.findTagById(id);
            TagDTO tagPatched = applyPatchToTag(patch, oldTag);

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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    private TagDTO applyPatchToTag(
            JsonPatch patch, TagDTO targetTag) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        JsonNode patched = patch.apply(objectMapper.convertValue(targetTag, JsonNode.class));
        return objectMapper.treeToValue(patched, TagDTO.class);
    }

    /**
     * Gets tag by id.
     * Get the most widely used tag of a user with the highest cost of all orders.
     * @return Tag instance.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/tags/popular", produces = {"application/hal+json"})
    public TagDTO getUsersMostWidelyUsedTag() {
        TagDTO tagDTO = tagService.getUsersMostWidelyUsedTag();
        Link selfLink = linkTo(methodOn(TagController.class)
                .findTagById(tagDTO.getId())).withSelfRel();
        Link tagsLink = linkTo(methodOn(TagController.class)
                .findAllTags(Optional.of(DEFAULT_PAGE_NUMBER), Optional.of(DEFAULT_PAGE_SIZE))).withRel("tags");
        tagDTO.add(tagsLink);
        tagDTO.add(selfLink);
        return tagDTO;
    }
}
