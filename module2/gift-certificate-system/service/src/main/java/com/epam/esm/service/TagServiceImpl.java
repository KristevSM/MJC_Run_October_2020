package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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

        return tagDao.findAll();
    }

    @Override
    public Tag findTagById(Long id) {

        return tagDao.find(id).orElseThrow(TagNotFoundException::new);
    }

    @Override
    public Long saveTag(Tag tag) {
        return null;
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

    }

    @Override
    public void assignDefaultTag(String tagName, Long certificateId) {
        Optional<Tag> tag = tagDao.findByTagName(tagName);
        if (!tag.isPresent()) {
            throw new NoSuchElementException("The tag not found");
        } else {
            Tag currentTag = tag.get();
            tagDao.assignTag(currentTag.getId(), certificateId);
        }
    }

    @Override
    public void addNewTagAndCertificate(String tagName, Long certificateId) {
        Optional<Tag> tag = tagDao.findByTagName(tagName);
        if (!tag.isPresent()) {
            throw new NoSuchElementException("The tag not found");
        } else {
            Tag currentTag = tag.get();
            tagDao.addNewTagAndToCertificate(currentTag.getId(), certificateId);
        }
    }
}
