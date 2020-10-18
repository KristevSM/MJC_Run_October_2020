package com.epam.esm.dao;

import com.epam.esm.model.Tag;

import java.util.Optional;

public interface TagDao extends CrudDAO<Tag> {
    Optional<Tag> findByTagName(String tagName);
    void assignDefaultTag(Long tagId, Long certificateId);
}
