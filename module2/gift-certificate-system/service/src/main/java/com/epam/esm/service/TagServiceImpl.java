package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;


@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<Tag> findAllTags() {

        List<Tag> tags = tagDao.findAll();
        if (tags.isEmpty()) {
            throw new TagNotFoundException("Tags were not found");
        } else {
            return tags;
        }
    }

    @Override
    public Tag findTagById(Long id) {
        Optional<Tag> tag = tagDao.find(id);
        if (!tag.isPresent()) {
            throw new TagNotFoundException(MessageFormat.format("Tag with id: {} not found", id));
        } else {
            return tag.get();
        }
    }

    @Override
    public Long saveTag(Tag tag) {
        Optional<Tag> tagOptional = tagDao.findByTagName(tag.getName());
        if (tagOptional.isPresent()) {
            throw new IllegalArgumentException(MessageFormat.format("Tag with name: {} already exists", tag.getName()));
        }
        return tagDao.save(tag);
    }

    @Override
    public void updateTag(Tag tag) {
        tagDao.update(tag);
    }

    @Override
    public void assignTag(Long tagId, Long certificateId) {
        tagDao.assignTag(tagId, certificateId);
    }

    @Override
    public void deleteTag(Long id) {
        tagDao.find(id).orElseThrow(() ->
                new TagNotFoundException(MessageFormat.format("Tag with id: {0} not found", id)));
        tagDao.delete(id);
    }

    @Override
    public void assignDefaultTag(String tagName, Long certificateId) {
        Optional<Tag> tag = tagDao.findByTagName(tagName);
        if (!tag.isPresent()) {
            throw new TagNotFoundException(MessageFormat.format("The tag with tag name: {} not found", tagName));
        } else {
            Tag currentTag = tag.get();
            tagDao.assignTag(currentTag.getId(), certificateId);
        }
    }

    @Override
    public void addNewTagAndCertificate(String tagName, Long certificateId) {
        Optional<Tag> tag = tagDao.findByTagName(tagName);
        if (!tag.isPresent()) {
            throw new TagNotFoundException(MessageFormat.format("The tag with tag name: {} not found", tagName));
        } else {
            Tag currentTag = tag.get();
            tagDao.addNewTagAndToCertificate(currentTag.getId(), certificateId);
        }
    }

    @Override
    public Tag findTagByTagName(String tagName) {
        Optional<Tag> tag = tagDao.findByTagName(tagName);
        if (!tag.isPresent()) {
            throw new TagNotFoundException(MessageFormat.format("The tag with tag name: {} not found", tagName));
        } else {
            return tag.get();
        }
    }
}
