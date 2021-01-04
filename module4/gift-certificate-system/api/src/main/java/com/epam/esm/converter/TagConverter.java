package com.epam.esm.converter;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagConverter {
    public TagDTO convertFromEntity(Tag tag) {
        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public Tag convertFromDTO(TagDTO tagDTO) {
        return Tag.builder()
                .id(tagDTO.getId())
                .name(tagDTO.getName())
                .build();
    }

    List<TagDTO> convertDTOsFromEntity(List<Tag> tags) {
        if (tags == null) {
            return new ArrayList<>();
        }
        return tags.stream()
                .map(tag -> TagDTO.builder()
                        .id(tag.getId())
                        .name(tag.getName())
                        .build())
                .collect(Collectors.toList());
    }

    List<Tag> convertFromDTOs(List<TagDTO> tagDTOs) {
        if (tagDTOs == null) {
            return new ArrayList<>();
        }
        return tagDTOs.stream()
                .map(tagDTO -> Tag.builder()
                        .id(tagDTO.getId())
                        .name(tagDTO.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
