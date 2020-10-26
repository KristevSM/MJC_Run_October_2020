package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.TagDaoJdbc;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TagServiceImplTest {

    private TagService tagService;

    private TagDao tagDao;
    private TagValidator tagValidator;

    @BeforeEach
    void setUp() {
        this.tagDao = mock(TagDaoJdbc.class);
        this.tagValidator = mock(TagValidator.class);
        this.tagService = new TagServiceImpl(tagDao, tagValidator);

    }

    @Test
    void shouldReturnAllTags() {

        List<Tag> tags = mock(ArrayList.class);
        Mockito.when(tags.size()).thenReturn(10);
        when(tagDao.findAll()).thenReturn(tags);

        assertEquals(10, tagService.findAllTags().size());
        Mockito.verify(tagDao, Mockito.times(1)).findAll();
    }

    @Test
    void shouldThrowExceptionWhenTagsNotFound() {
        List<Tag> emptyList = new ArrayList<>();
        when(tagDao.findAll()).thenReturn(emptyList);

        assertThrows(TagNotFoundException.class, () -> {
            tagService.findAllTags();
        });
    }

    @Test
    void shouldFindTagById() {

        Tag tag = mock(Tag.class);
        when(tagDao.find(1L)).thenReturn(Optional.of(tag));

        assertEquals(tag, tagService.findTagById(1L));
        Mockito.verify(tagDao, Mockito.times(1)).find(1L);
    }

    @Test
    void shouldThrowExceptionWhenTagByIdNotFound() {

        Tag tag = new Tag();
        tag.setId(1L);
        when(tagDao.find(tag.getId())).thenThrow(TagNotFoundException.class);

        assertThrows(TagNotFoundException.class, () -> {
            tagService.findTagById(tag.getId());
        });
        Mockito.verify(tagDao, Mockito.times(1)).find(1L);
    }

    @Test
    void shouldSaveTag() {

        Tag tag = mock(Tag.class);
        when(tagDao.save(tag)).thenReturn(1L);
        Long expectedId = 1L;
        assertEquals(expectedId, tagService.saveTag(tag));
        Mockito.verify(tagDao, Mockito.times(1)).save(tag);
    }

    @Test
    void shouldThrowExceptionWhenTagNotSaved() {

        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tag 1");
        when(tagDao.save(tag)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            tagService.saveTag(tag);
        });
        Mockito.verify(tagDao, Mockito.times(1)).findByTagName("tag 1");
        Mockito.verify(tagDao, Mockito.times(1)).save(tag);
    }

    @Test
    void shouldUpdateTag() {

        Tag tag = mock(Tag.class);
        tagService.updateTag(tag);
        Mockito.verify(tagDao, Mockito.times(1)).update(tag);

    }

    @Test
    void shouldAssignTag() {

        Tag tag = new Tag();
        tag.setId(1L);
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1L);
        tagService.assignTag(tag.getId(), certificate.getId());
        Mockito.verify(tagDao, Mockito.times(1)).assignTag(tag.getId(), certificate.getId());

    }

    @Test
    void shouldAssignDefaultTag() {

        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tag test");
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1L);
        when(tagDao.findByTagName(tag.getName())).thenReturn(Optional.of(tag));
        tagService.assignDefaultTag(tag.getName(), certificate.getId());
        Mockito.verify(tagDao, Mockito.times(1)).assignTag(tag.getId(), certificate.getId());
        Mockito.verify(tagDao, Mockito.times(1)).findByTagName(tag.getName());

    }

    @Test
    void shouldDeleteTag() {

        Tag tag = mock(Tag.class);
        when(tagDao.find(1L)).thenReturn(Optional.ofNullable(tag));
        tagService.deleteTag(1L);
        Mockito.verify(tagDao, Mockito.times(1)).find(1L);
        Mockito.verify(tagDao, Mockito.times(1)).delete(1L);

    }

    @Test
    void shouldThrowExceptionDeleteCertificate() {

        when(tagDao.find(1L)).thenThrow(TagNotFoundException.class);
        assertThrows(TagNotFoundException.class, () -> {
            tagService.deleteTag(1L);
        });
        Mockito.verify(tagDao, Mockito.times(1)).find(1L);
        Mockito.verify(tagDao, Mockito.times(0)).delete(1L);
    }

    @Test
    void shouldAddNewTagAndCertificate() {

        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tag 2");
        when(tagDao.findByTagName(tag.getName())).thenReturn(Optional.of(tag));

        tagService.addNewTagAndCertificate("tag 2", tag.getId());
        Mockito.verify(tagDao, Mockito.times(1)).findByTagName("tag 2");

    }

    @Test
    void shouldFindTagByTagName() {

        Tag tag = mock(Tag.class);
        when(tagDao.findByTagName("tag 2")).thenReturn(Optional.ofNullable(tag));
        tagService.findTagByTagName("tag 2");
        Mockito.verify(tagDao, Mockito.times(1)).findByTagName("tag 2");

    }
}