package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.model.Tag;

import java.util.List;

public interface TagService {

    List<TagDTO> findAllTags(Long page, Long pageSize);

    TagDTO findTagById(Long id);

    Long saveTag(TagDTO tagDTO);

    void updateTag(TagDTO tagDTO);

    void deleteTag(Long id);

    TagDTO getUsersMostWidelyUsedTag();
}
