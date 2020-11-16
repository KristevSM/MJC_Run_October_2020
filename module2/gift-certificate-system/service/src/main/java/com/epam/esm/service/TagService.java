package com.epam.esm.service;

import com.epam.esm.model.Tag;
import java.util.List;

public interface TagService {

    List<Tag> findAllTags(int from, int pageSize);

    Tag findTagById(Long id);

    Long saveTag(Tag tag);

    void updateTag(Tag tag);

    void deleteTag(Long id);

    Tag getUsersMostWidelyUsedTag();
}
