package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

/**
 * @author Sergei Kristev
 * <p>
 * Service for managing Tag objects.
 */
@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final TagValidator tagValidator;

    /**
     * Constructor accepts TagDao object.
     *
     * @param tagDao        TagDao instance.
     * @param tagValidator  TagValidator instance.
     */
    @Autowired
    public TagServiceImpl(TagDao tagDao, TagValidator tagValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
    }

    /**
     * Gets list of all tags from TagDao.
     *
     * @return Tags list.
     */
    @Override
    public List<Tag> findAllTags(int from, int pageSize) {

        List<Tag> tags = tagDao.findAll(from, pageSize);
        if (tags.isEmpty()) {
            throw new TagNotFoundException("Tags were not found");
        } else {
            return tags;
        }
    }

    /**
     * Gets tag by id.
     *
     * @param id Tag id.
     * @return Tag instance.
     */
    @Override
    public Tag findTagById(Long id) {
        return tagDao.find(id).orElseThrow(() -> new TagNotFoundException(MessageFormat
                .format("Tag with id: {0} not found", id)));
    }

    /**
     * Saves new tag.
     *
     * First, finds a tag by tag name. Subsequently, if the tag record is not exists, method saves tag through <i>tagDao</i>, else
     * throw IllegalArgumentException;
     *
     * @param tag Tag instance.
     * @return Tag id.
     * @throws IllegalArgumentException Tag with name: {0} already exists.
     */
    @Transactional
    @Override
    public Long saveTag(Tag tag) {
        Optional<Tag> tagOptional = tagDao.findByTagName(tag.getName());
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
        return tagDao.save(tag);
    }

    /**
     * Updates tag.
     * <p>
     * Updating tag through <i>tagDao</i>.
     *
     * @param tag Tag instance.
     */
    @Transactional
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
            tagDao.update(tag);
        }
    }

    /**
     * Deletes tag.
     * <p>
     * First, finds a tag by ID. Subsequently, if the tag record is exists, method deletes tag through <i>tagDao</i>, else
     * throw TagNotFoundException;
     *
     * @param id Tag id.
     * @throws TagNotFoundException Tag with id: {0} not found
     */
    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagDao.find(id).orElseThrow(() ->
                new TagNotFoundException(MessageFormat.format("Tag with id: {0} not found", id)));
        tagDao.delete(id);
    }
}
