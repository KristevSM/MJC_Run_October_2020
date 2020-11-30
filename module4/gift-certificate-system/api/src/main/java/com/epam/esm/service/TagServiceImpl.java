package com.epam.esm.service;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagValidator tagValidator;
    private final TagConverter tagConverter;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagValidator tagValidator, TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagValidator = tagValidator;
        this.tagConverter = tagConverter;
    }

    @Override
    public List<TagDTO> findAllTags(int page, int pageSize) {
        Page<Tag> tagPage = tagRepository.findAll(PageRequest.of(page, pageSize));
        List<Tag> tagList = tagPage.toList();
        return tagList.stream()
                .map(tagConverter::convertTagDtoFromTag)
                .collect(Collectors.toList());
    }

    @Override
    public TagDTO findTagById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException(MessageFormat
                .format("Tag with id: {0} not found", id)));
        return tagConverter.convertTagDtoFromTag(tag);
    }

    @Override
    public Long saveTag(TagDTO tagDTO) {
        Optional<Tag> tagOptional = tagRepository.findByName(tagDTO.getName());
        if (tagOptional.isPresent()) {
            throw new IllegalArgumentException(MessageFormat.format("Tag with name: {0} already exists", tagDTO.getName()));
        }
        Tag tag = tagConverter.convertTagFromTagDTO(tagDTO);
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
    public void updateTag(TagDTO tagDTO) {
        Tag tag = tagConverter.convertTagFromTagDTO(tagDTO);
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
    public TagDTO getUsersMostWidelyUsedTag() {
        Tag tag = tagRepository.getUsersMostWidelyUsedTag().orElseThrow(() ->
                new TagNotFoundException("Tag not found"));
        return tagConverter.convertTagDtoFromTag(tag);
    }
}
