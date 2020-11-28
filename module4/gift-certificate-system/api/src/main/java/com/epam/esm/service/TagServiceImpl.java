package com.epam.esm.service;

import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagValidator tagValidator) {
        this.tagRepository = tagRepository;
        this.tagValidator = tagValidator;
    }

    @Override
    public List<Tag> findAllTags(Long page, Long pageSize) {
        return tagRepository.findAll();
    }

    @Override
    public Tag findTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException(MessageFormat
                .format("Tag with id: {0} not found", id)));    }

    @Override
    public Long saveTag(Tag tag) {
        Optional<Tag> tagOptional = tagRepository.findByName(tag.getName());
        if (tagOptional.isPresent()) {
            throw new IllegalArgumentException(MessageFormat.format("Tag with name: {0} already exists", tag.getName()));
        }
        BindingResult result = new BeanPropertyBindingResult(tag, "tag");
        tagValidator.validate(tag, result);
        if (result.hasErrors()) {
            String brokenField = result.getFieldErrors().get(0).getField();
            String errorCode = result.getFieldErrors().get(0).getCode();
            throw new InvalidInputDataException(MessageFormat.format("Unexpected tag''s field: {0}, error code: {1}",
                    brokenField, errorCode));
        }
        Tag newTag = tagRepository.save(tag);
        return newTag.getId();    }

    @Override
    public void updateTag(Tag tag) {
        BindingResult result = new BeanPropertyBindingResult(tag, "tag");
        tagValidator.validate(tag, result);
        if (result.hasErrors()) {
            String brokenField = result.getFieldErrors().get(0).getField();
            String errorCode = result.getFieldErrors().get(0).getCode();
            throw new InvalidInputDataException(MessageFormat.format("Unexpected tag''s field: {0}, error code: {1}",
                    brokenField, errorCode));
        } else {
            Optional<Tag> tagFromDao = tagRepository.findByName(tag.getName());
            if (tagFromDao.isPresent()) {
                throw new InvalidInputDataException(MessageFormat.format("Tag with name: {0} already exists", tag.getName()));
            }
            tagRepository.save(tag);
        }
    }

    @Override
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() ->
                new TagNotFoundException(MessageFormat.format("Tag with id: {0} not found", id)));
        tagRepository.delete(tag);
    }

    @Override
    public Tag getUsersMostWidelyUsedTag() {
        return tagRepository.getUsersMostWidelyUsedTag().orElseThrow(() ->
                new TagNotFoundException("Tag not found"));    }
}
