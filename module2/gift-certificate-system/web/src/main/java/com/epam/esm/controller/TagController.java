package com.epam.esm.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gift-certificates")
public class TagController {

    private final TagService tagService;
    private final TagValidator tagValidator;

    public TagController(TagService tagService, TagValidator tagValidator) {
        this.tagService = tagService;
        this.tagValidator = tagValidator;
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Integer> updateGiftCertificate(@PathVariable Long id, @RequestBody JsonPatch patch) {
        try {
            Tag oldTag = tagService.findTagById(id);
            Tag tagPatched = applyPatchToTag(patch, oldTag);
            tagService.updateTag(tagPatched);
            return ResponseEntity.ok().build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private Tag applyPatchToTag(
            JsonPatch patch, Tag targetTag) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JsonNode patched = patch.apply(objectMapper.convertValue(targetTag, JsonNode.class));
        return objectMapper.treeToValue(patched, Tag.class);
    }

    @GetMapping(value = "/tags")
    public List<Tag> findAll() {
        return tagService.findAllTags();
    }
}
