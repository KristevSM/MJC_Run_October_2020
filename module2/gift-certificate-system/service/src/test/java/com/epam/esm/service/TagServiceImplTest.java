package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.TagDaoJdbc;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.Tag;
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

    @Mock
    private TagDao tagDao;

    @BeforeEach
    void setUp() {
        this.tagDao = mock(TagDaoJdbc.class);
        this.tagService = new TagServiceImpl(tagDao);

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
    void shouldFindTagById() {

        Tag tag = mock(Tag.class);
        when(tagDao.find(1L)).thenReturn(Optional.of(tag));

        assertEquals(tag, tagService.findTagById(1L));
        Mockito.verify(tagDao, Mockito.times(1)).find(1L);
    }

    @Test
    void shouldThrowExceptionWhenTagByIdNotFound() {

        Tag tag = mock(Tag.class);
        when(tagDao.find(2L)).thenReturn(Optional.of(tag));

        assertThrows(TagNotFoundException.class, () -> {
            tagService.findTagById(1L);
        });
        Mockito.verify(tagDao, Mockito.times(1)).find(1L);
    }

    @Test
    void Tag() {

        Tag tag = mock(Tag.class);
        when(tagDao.save(tag)).thenReturn(1L);
        Long expectedId = 1L;
        assertEquals(expectedId, tagService.saveTag(tag));
        Mockito.verify(tagDao, Mockito.times(1)).save(tag);
    }

    @Test

    void shouldUpdateTag() {

        Tag tag = mock(Tag.class);
        tagService.updateTag(tag);
        Mockito.verify(tagDao, Mockito.times(1)).update(tag);

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

}