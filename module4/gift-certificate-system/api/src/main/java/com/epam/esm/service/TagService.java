package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.model.Tag;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TagService {

    Page<TagDTO> findAllTags(int page, int pageSize);

    TagDTO findTagById(Long id);

    TagDTO saveTag(TagDTO tagDTO);

    TagDTO updateTag(TagDTO tagDTO);

    void deleteTag(Long id);

    TagDTO getUsersMostWidelyUsedTag();
}
