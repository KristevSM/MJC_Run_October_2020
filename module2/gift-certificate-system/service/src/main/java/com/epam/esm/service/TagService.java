package com.epam.esm.service;

import com.epam.esm.model.Tag;
import java.util.List;
import java.util.Optional;

public interface TagService {

    List<Tag> findAllTags();
    Optional<Tag> findTagById(Long id);
    Long saveTag(Tag tag);
    void updateTag(Tag tag);
    void deleteTag(Long id);
    void assignDefaultTag(String tagName, Long certificateId);
}
